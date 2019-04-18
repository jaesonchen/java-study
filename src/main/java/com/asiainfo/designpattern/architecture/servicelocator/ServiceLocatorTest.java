package com.asiainfo.designpattern.architecture.servicelocator;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月19日  下午5:13:12
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ServiceLocatorTest {

    public static void main(String[] args) {
        
        // app init
        AppContext context = init();
        // service locator init
        ServiceLocator.setAppContext(context);
        // byName
        Service service = ServiceLocator.getBean("nameService", Service.class);
        service.perform();
        // byType
        service = ServiceLocator.getBean(MyService.class);
        service.perform();
    }
    
    static AppContext init() {
        
        AppContext context = new AppContext();
        context.addBean(new MyService());
        context.addBean(new NameService());
        return context;
    }
    
    interface Service {
        void perform();
    }
    
    static class MyService implements Service {
        @Override
        public void perform() {
            System.out.println("MyService perform");
        }
    }
    
    static class NameService implements Service {
        @Override
        public void perform() {
            System.out.println("NameService perform");
        }
    }
}
