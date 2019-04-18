package com.asiainfo.designpattern.architecture.aggregatorservice;

/**
 * 当您需要统一的API时，无论客户端设备有多少种，使用Aggregator Microservices模式。
 * 通常用于聚合多个微服务调用。
 * 
 * @author       zq
 * @date         2018年4月26日  下午5:39:12
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class AggregatorMicroServices {

    public static void main(String[] args) {
        System.out.println(aggregator("101"));
    }
    
    // aggregator
    public static Product aggregator(String id) {
        Product product = new Product(id);
        // micro service1
        product.setTitle(new InfoService().getTitle(id));
        // micro service2
        product.setInventory(new InventoryService().getInventory(id));
        return product;
    }

    // micro service(rpc/rest)
    static class InfoService {
        public String getTitle(String id) {
            return "product " + id;
        }
    }
    static class InventoryService {
        public int getInventory(String id) {
            return 10;
        }
    }
    
    static class Product {
        
        String id;
        String title;
        int inventory;
        public Product(String id) {
            this.id = id;
        }
        
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public int getInventory() {
            return inventory;
        }
        public void setInventory(int inventory) {
            this.inventory = inventory;
        }
        @Override
        public String toString() {
            return "Product [id=" + id + ", title=" + title + ", inventory=" + inventory + "]";
        }
    }
}
