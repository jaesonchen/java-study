package com.asiainfo.designpattern.structure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * 通过对象组合的方式，Bridge模式把两个角色之间的继承关系改为了耦合的关系，从而使这两者可以从容自若的各自独立的变化，这也是Bridge模式的本意。
 * bridge是在设计之初的模式，adapter是针对已有系统的代码。
 * jdbc api使用bridge模式。
 * 
 * Facade模式注重简化接口，Adapter模式注重转换接口，Bridge模式注重分离接口（抽象）与其实现，Decorator模式注重稳定接口的前提下为对象扩展功能。
 * 
 * public interface Implementor {
 *      public void operationImpl();
 * }
 * class ConcreteImplementorA implements Implementor {
 *      @Override
 *      public void operationImpl() {
 *          // 真正的实现
 *          System.out.println("具体实现A");
 *      }
 * }
 * class ConcreteImplementorB implements Implementor {
 *      @Override
 *      public void operationImpl() {
 *          // 真正的实现
 *          System.out.println("具体实现B");
 *      }
 * }
 * public abstract class Abstraction {
 *      protected Implementor implementor;
 *      public Abstraction(Implementor implementor) {
 *          this.implementor = implementor;
 *      }
 *      public abstract void operation();
 * }
 * class RefinedAbstraction extends Abstraction {
 *
 *      public RefinedAbstraction(Implementor implementor) {
 *          super(implementor);
 *      }
 *      @Override
 *      public void operation() {
 *          //do someting 
 *          super.operation();
 *          //do other thing
 *      }
 * }
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:29:54
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Bridge {

    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {

        DriverManager.registerDriver(new MySqlDriver());
        IConnection mysql = DriverManager.getConnection();
        mysql.query("select * from table");
        
        DriverManager.registerDriver(new OracleDriver());
        IConnection oracle = DriverManager.getConnection(DIALECT.ORACLE);
        oracle.query("insert into table");
        
        IConnection db2 = DriverManager.getConnection(DIALECT.DB2);
        db2.query("update table");
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
    
    //mysql驱动程序实现
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
