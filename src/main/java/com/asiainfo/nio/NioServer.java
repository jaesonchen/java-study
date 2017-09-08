package com.asiainfo.nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 水平触发(level-triggered，也被称为条件触发)LT:只要满足条件，就触发一个事件(只要有数据没有被获取，内核就不断通知你)。
 * 				  边缘触发(edge-triggered)ET: 每当状态变化时，触发一个事件。
 *               Java的NIO属于水平触发，即条件触发。
 *               水平触发和条件触发在IO编程的区别：
 *               假定经过长时间的沉默后，来了100个字节，这时无论边缘触发和条件触发都会产生一个read ready notification通知应用程序可读。
 *               应用程序读了50个字节，然后重新调用API等待io事件。这时条件触发的api会因为还有50个字节可读从 而立即返回用户一个read ready notification。
 *               而边缘触发的api会因为可读这个状态没有发生变化而陷入长期等待。 
 *               因此在使用边缘触发的api时，要注意每次都要读到socket返回EWOULDBLOCK为止，否则这个socket就算废了。
 *               而使用条件触发的API 时，如果应用程序不需要写就不要关注socket可写的事件，否则就会无限次的立即返回一个write ready notification。
 *               常用的select就是属于条件触发这一类，长期关注socket写事件会出现CPU 100%的毛病。
 *               所以在使用Java的NIO编程的时候，在没有数据可以往外写的时候要取消写事件，在有数据往外写的时候再注册写事件。
 * 
 * @author       zq
 * @date         2017年8月11日  下午12:29:23
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class NioServer implements Runnable {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
    final int BUF_SIZE = 1024;
    final int PORT = 8080;
    final int TIMEOUT = 3000;
    
    private Selector selector;
    private ServerSocketChannel ssChannel;
    
    public NioServer() {

        try {
        	logger.info("open Selector ...");
            this.selector = Selector.open();
            logger.info("open ServerSocketChannel ...");
            this.ssChannel= ServerSocketChannel.open();
            logger.info("bind InetSocketAddress {} ...", new InetSocketAddress(PORT));
            this.ssChannel.socket().bind(new InetSocketAddress(PORT));
            logger.info("configureBlocking {} ...", false);
            this.ssChannel.configureBlocking(false);
            //ServerSocketChannel register accept event
            //SocketChannel register connect event
            logger.info("register intrestops {} ...", SelectionKey.OP_ACCEPT);
            this.ssChannel.register(this.selector, SelectionKey.OP_ACCEPT);
            logger.info("ServerSocketChannel establish ...");
        } catch (IOException ex) {
            logger.error("error on create ServerSocketChannel!", ex);
        }
    }

	/* 
	 * @Description: TODO
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		logger.info("begin select socket event ...");
		try {
			selector();
        } catch (IOException ex) {
        	logger.error("error on handle socket event!", ex);
		} finally {
            try {
                if (this.selector != null) {
                	this.selector.close();
                }
                if (ssChannel != null) {
                	this.ssChannel.close();
                }
            } catch (IOException ex) {
            	logger.error("error on close channel!", ex);
            }
        }
	}
	
    public void selector() throws IOException {
 
    	while (!Thread.currentThread().isInterrupted()) {
    		//wakeup after timeout
    		if (selector.select(TIMEOUT) == 0) {
    			logger.info("no event coming ...");
    			continue;
    		}
    		Iterator<SelectionKey> it = selector.selectedKeys().iterator();
    		while (it.hasNext()) {
    			SelectionKey key = it.next();
    			it.remove();
    			//invalid key
    			if (!key.isValid()) {
    				logger.warn("invalid key:{}", key);
    				continue;
    			}
    			try {
    				//client connect request coming
    				if (key.isAcceptable()) {
    					handleAccept(key);
    				}
    				//socket input buffer has data, remote client socket close, 
    				if (key.isReadable()) {
    					handleRead(key);
    				}
    				//socket send buffer is not full
    				if (key.isWritable()) {
    					handleWrite(key);
    				}
    			} catch (Exception ex) {
    				logger.error("error on handle socket event!", ex);
    				try {
    					key.cancel();
						key.channel().close();
					} catch (Exception e) {
						logger.error("error on close channel:{}\nerror message:{}", key.channel(), ex);
					}
    			}
    		}
    	}
    }
    
    public void handleAccept(SelectionKey key) throws IOException {
    	
    	logger.info("ServerSocketChannel handle accept ...");
        ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = ssChannel.accept();
        channel.configureBlocking(false);
        //register read event for client socket connection
        channel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocateDirect(BUF_SIZE));
    }

    public void handleRead(SelectionKey key) throws IOException {
    	
    	logger.info("SocketChannel handle read ...");
    	ByteArrayOutputStream result = new ByteArrayOutputStream();
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buff = (ByteBuffer) key.attachment();
        int read = 0;
        while ((read = channel.read(buff)) > 0) {
            buff.flip();
            byte[] bt = new byte[buff.remaining()];
            buff.get(bt);
            result.write(bt);
            buff.clear();
        }

        //eof
        if (-1 == read) {
        	key.cancel();
        	channel.close();
        	return;
        }
        
        logger.info("request from client:{}, message={}", channel.getRemoteAddress(), new String(result.toByteArray(), "utf-8"));
        //unregister
        //key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
        //this will always trigger write ready notification
        key.interestOps(SelectionKey.OP_WRITE);
        logger.info("SocketChannel register intrestops {} ...", SelectionKey.OP_WRITE);
        //register need acquire two lock
        //channel.register(key.selector(), SelectionKey.OP_WRITE);
    }

    public void handleWrite(SelectionKey key) throws IOException {
    	
    	logger.info("SocketChannel handle write ...");
        ByteBuffer output = ByteBuffer.wrap(("result=" + System.currentTimeMillis()).getBytes("utf-8"));
        SocketChannel channel = (SocketChannel) key.channel();
        while (output.hasRemaining()) {
        	channel.write(output);
        }
        logger.info("write response to client: {}", new String(output.array(), "utf-8"));
        output.compact();
        key.interestOps(SelectionKey.OP_READ);
        logger.info("SocketChannel register intrestops {} ...", SelectionKey.OP_READ);
        //channel.socket().getKeepAlive()
        //channel.shutdownOutput();//eof
    	//key.cancel();
        //channel.close();
    }
    
    /**
     * @Description: TODO
     * 
     * @param args
     */
    public static void main(String[] args) {
    	new Thread(new NioServer()).start();
    }
}
