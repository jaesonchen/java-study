package com.asiainfo.nio;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月13日  下午9:07:15
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ServerSocketConfigure {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		
		//绑定端口 BindException
		// 1. 端口已经被其他服务器进程占用.
		// 2. 在某些操作系统中, 如果没有以超级用户的身份来运行服务器程序, 那么操作系统不允许服务器绑定到 1-1023 之间的端口.
		// ServerSocket serverSocket = new ServerSocket(80);
		
		//设定客户连接请求队列的长度
		// 客户端执行 Socket socket = new Socket("localhost", 80); 就意味着在主机的 80 端口上, 监听到了一个客户的连接请求.
		// 管理客户连接请求的任务是由操作系统来完成的. 操作系统把这些连接请求存储在一个先进先出的队列中. 许多操作系统限定了队列的最大长度, 一般为 50 . 
		// 当队列中的连接请求达到了队列的最大容量时, 服务器进程所在的主机会拒绝新的连接请求. 只有当服务器进程通过 ServerSocket 的 accept() 方法从队列中取出连接请求, 
		// 使队列腾出空位时, 队列才能继续加入新的连接请求.
		// 对于客户进程, 如果它发出的连接请求被加入到服务器的请求连接队列中, 就意味着客户与服务器的连接建立成功, 客户进程从 Socket 构造方法中正常返回. 
		// 如果客户进程发出的连接请求被服务器拒绝, Socket 构造方法就会抛出 ConnectionException.
		// Tips: 创建绑定端口的服务器进程后, 当客户进程的 Socket构造方法返回成功, 表示客户进程的连接请求被加入到服务器进程的请求连接队列中. 
		// 虽然客户端成功返回 Socket对象, 但是还没跟服务器进程形成一条通信线路. 必须在服务器进程通过 ServerSocket 的 accept() 方法从请求连接队列中取出连接请求, 
		// 并返回一个Socket 对象后, 服务器进程这个Socket 对象才与客户端的 Socket 对象形成一条通信线路.
		// ServerSocket 构造方法的 backlog 参数用来显式设置连接请求队列的长度, 它将覆盖操作系统限定的队列的最大长度. 值得注意的是, 在以下几种情况中, 
		// 仍然会采用操作系统限定的队列的最大长度:
		// 1. backlog 参数的值大于操作系统限定的队列的最大长度;
		// 2. backlog 参数的值小于或等于0;
		// 3. 在ServerSocket 构造方法中没有设置 backlog 参数.
		// ServerSocket serverSocket = new ServerSocket(port, 10);

		//设定绑定的IP 地址
		// ServerSocket(int port, int backlog, InetAddress bingAddr)
		// 如果主机只有一个IP 地址, 那么默认情况下, 服务器程序就与该IP 地址绑定.
		// bindAddr 参数, 显式指定服务器要绑定的IP 地址, 该构造方法适用于具有多个IP 地址的主机.
		// InetAddress.getByName("192.168.1.1") 
		
		//默认构造方法的作用
		// 默认构造方法的用途是, 允许服务器在绑定到特定端口之前, 先设置ServerSocket 的一些选项. 因为一旦服务器与特定端口绑定, 有些选项就不能再改变了.
		// ServerSocket serverSocket = new ServerSocket();
		// serverSocket.setReuseAddress(true);
		// serverSocket.bind(new InetSocketAddress(8080));
	}
}
