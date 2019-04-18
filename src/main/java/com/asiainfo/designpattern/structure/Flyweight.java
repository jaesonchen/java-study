package com.asiainfo.designpattern.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 意图: 运用共享技术有效地支持大量细粒度的对象，典型应用：数据库连接池、线程池。
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:31:23
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Flyweight {

    public static void main(String[] args) {

        ConnectionPool pool = new ConnectionPool();
        IConnection con = pool.getConnection();
        con.query("select * from user");
        pool.release(con);
        
        pool = new ConnectionPool(new OracleDriver());
        con = pool.getConnection();
        con.query("select * from user");
        pool.release(con);
    }

    enum DIALECT {
        MYSQL, ORACLE, DB2;
    }
    interface IDriver {
        public DIALECT getDialect();
        public IConnection getConnection();
    }
    interface IConnection {
        public void query(String sql);
    }
    
    //连接池实现
    static class ConnectionPool {
        
        private List<IConnection> pool;
        private int poolSize = 10;
        
        public ConnectionPool() {
            this(new MySqlDriver());
        }
        public ConnectionPool(IDriver driver) {
            pool = new ArrayList<IConnection>(poolSize);
            DriverManager.registerDriver(driver);
            for (int i = 0; i < poolSize; i++) {
                IConnection con = DriverManager.getConnection(driver.getDialect());
                pool.add(con);
            }
        }
        public synchronized void release(IConnection con) {
            pool.add(con);
            System.out.println("release connection, poolsize=" + pool.size());
        }
        public synchronized IConnection getConnection() {
            if (pool.size() > 0) {
                IConnection con = pool.remove(0);
                System.out.println("get connection, remain=" + pool.size());
                return con;
            } else {
                return null;
            }
        }
    }
    
    //驱动管理
    static class DriverManager {
        
        private static final DIALECT DEFAULT_DIALECT = DIALECT.MYSQL;
        private static Map<DIALECT, IDriver> drivers = new ConcurrentHashMap<DIALECT, IDriver>();

        private DriverManager() {}
        public static void registerDriver(IDriver driver) {
            drivers.put(driver.getDialect(), driver);
        }
        
        public static IConnection getConnection() {
            return getConnection(DEFAULT_DIALECT); 
        }
        
        public static IConnection getConnection(DIALECT dialect) {
            IDriver driver = drivers.get(dialect);
            if (driver == null) {
                throw new IllegalArgumentException("no driver register with " + dialect);
            }
            return driver.getConnection();
        }
    }
    
    //驱动程序实现
    static class MySqlDriver implements IDriver {
        
        @Override public IConnection getConnection() {
            return new IConnection() {
                @Override public void query(String sql) {
                    System.out.println("MySql query ('" + sql + "') is executing......");
                }
            };
        }
        @Override public DIALECT getDialect() {
            return DIALECT.MYSQL;
        }
    }
    
    //oracle驱动程序实现
    static class OracleDriver implements IDriver {
        
        @Override public IConnection getConnection() {
            return new IConnection() {
                @Override public void query(String sql) {
                    System.out.println("Oracle query ('" + sql + "') is executing......");
                }
            };
        }
        @Override public DIALECT getDialect() {
            return DIALECT.ORACLE;
        }
    }

    //db2驱动程序实现
    static class DB2Driver implements IDriver {
        
        @Override public IConnection getConnection() {
            return new IConnection() {
                @Override public void query(String sql) {
                    System.out.println("DB2 query ('" + sql + "') is executing......");
                }
            };
        }
        @Override public DIALECT getDialect() {
            return DIALECT.DB2;
        }
    }
}
