package com.asiainfo.arm;

/**   
 * Java 7 的编译器和运行环境支持新的 try-with-resources 语句，称为 ARM 块(Automatic Resource Management) ，自动资源管理。
 * 
 * try{} finally {}代码通常都是在finally里写资源关闭代码，以保证try块出现异常时，finally的资源关闭一定会被执行； 但是当try块抛出异常，
 * finally的资源关闭代码也抛出异常时，try块的异常将被finally块的异常抑制，finally的异常将根据调用栈向往传播，即使try语句块中抛出的异常与异常传播更相关。
 * 
 * try-with-resources结构里不仅能够操作java内置的类。你也可以在自己的类中实现java.lang.AutoCloseable接口，然后在try-with-resources结构里使用这个类。
 * 
 * 
 * try finally特性：
 * try中的return语句会将返回结果/抛出异常压栈，然后转入到finally子过程，等到finally子过程执行完毕之后（没有return），再返回/抛出异常。
 * finally的语句是在方法return之前执行的，而且如果finally中有return语句的话，方法直接结束，不再返回栈中的值/异常。
 * finally中return会吃掉try中抛出的异常，方法不需要声明异常。
 * finally中抛出异常会抑制try中抛出的异常，方法需要声明finally抛出得异常类型。
 * 
 * 
 * @author chenzq  
 * @date 2019年3月19日 下午1:14:01
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class TryWithResources {

    public static void main(String[] args) throws Exception {
        
        // 传统资源管理
        traditionResourceManager();
        gracefulResourceManager();
        
        // try异常抑制
        try {
            traditionResourceManagerWithException();
        } catch (BusinessException be) {
            be.printStackTrace();
        } catch (ResourcesCloseException rce) {
            rce.printStackTrace();
        }
        
        // TryWithResources
        tryWithResources();
        
        // TryWithResources 带异常
        try {
            tryWithResourcesException();
        } catch (BusinessException be) {
            be.printStackTrace();
        } catch (ResourcesCloseException rce) {
            rce.printStackTrace();
        }
    }

    // 传统的资源管理
    static void traditionResourceManager() throws Exception {
        MyResources res = null;
        try {
            res = new MyResources();
            res.service();
        } finally {
            if (null != res) {
                res.close();
            }
        }
    }
    
    // 传统优雅的资源管理
    static void gracefulResourceManager() throws Exception {
        MyResources res = null;
        try {
            res = new MyResources();
            res.service();
        } finally {
            close(res);
        }
    }
    // 通用工具方法
    static void close(AutoCloseable... closeables) {
        if (null != closeables) {
            for (AutoCloseable closeable : closeables) {
                try {
                    closeable.close();
                } catch (Exception ex) {
                    //ignore
                }
            }
        }
    }
    
    // 传统的资源管理，try异常抑制
    static void traditionResourceManagerWithException() throws Exception {
        
        WithExceptionResources res = null;
        try {
            res = new WithExceptionResources();
            res.service();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (null != res) {
                res.close();
            }
        }
    }
    
    // TryWithResources，资源关闭与try()中打开顺序相反
    static void tryWithResources() {
        
        try (MyResources res = new MyResources();
                MyResources2 res2 = new MyResources2();) {
            
            res.service();
            res2.service();
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (ResourcesCloseException e) {
            e.printStackTrace();
        }
    }
    
    // TryWithResources，资源关闭异常不会抑制try{}业务异常
    static void tryWithResourcesException() throws Exception {
        
        try (WithExceptionResources res = new WithExceptionResources()) {
            res.service();
        }
    }
}

//自定义资源1
class MyResources implements AutoCloseable {
    
    public void service() throws BusinessException {
        System.out.println("MyResources service execute!");
    }

    @Override
    public void close() throws ResourcesCloseException {
        System.out.println("MyResources closed!");
    }
}

//自定义资源2
class MyResources2 implements AutoCloseable {
    
    public void service() throws BusinessException {
        System.out.println("MyResources2 service execute!");
    }

    @Override
    public void close() throws ResourcesCloseException {
        System.out.println("MyResources2 closed!");
    }
}

//带异常的资源
class WithExceptionResources implements AutoCloseable {

    public void service() throws BusinessException {
        System.out.println("WithExceptionResources service with exception!");
        throw new BusinessException();
    }
    
    @Override
    public void close() throws ResourcesCloseException {
        System.out.println("WithExceptionResources closed with exception!");
        throw new ResourcesCloseException();
    }
}

class ResourcesCloseException extends Exception {
    private static final long serialVersionUID = 1L;
    public ResourcesCloseException() {}
    public ResourcesCloseException(String msg) {
        super(msg);
    }
}

class BusinessException extends Exception {
    private static final long serialVersionUID = 1L;
    public BusinessException() {}
    public BusinessException(String msg) {
        super(msg);
    }
}
