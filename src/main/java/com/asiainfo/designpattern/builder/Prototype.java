package com.asiainfo.designpattern.builder;

/**
 * 原型模式：通过java clone实现对象的创建
 * 
 * Cloneable的浅克隆、深克隆
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:34:13
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Prototype {

    public static void main(String[] args) throws CloneNotSupportedException {
        
        User chenzq = new User("1001", "chenzq", new Address("bj", 10010));
        // 1
        User jaeson = chenzq.clone();
        jaeson.setUserName("jaeson");
        jaeson.getAddress().setCity("sh");
        // 2
        UserFactory factory = new UserFactory(chenzq);
        User jaesonchen = factory.createUser();
        jaesonchen.setUserName("jaesonchen");
        jaesonchen.getAddress().setCity("gz");
        
        System.out.println("chenzq=" + chenzq);
        System.out.println("jaeson=" + jaeson);
        System.out.println("jachen=" + jaesonchen);
    }

    static class UserFactory {
        
        private User user;
        public UserFactory(User user) {
            this.user = user;
        }
        
        public User createUser() {
            try {
                return this.user.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    
    static class User implements Cloneable {
        
        private String userId;
        private String userName;
        private Address address;
        
        public User(String userId, String userName, Address address) {
            this.userId = userId;
            this.userName = userName;
            this.address = address;
        }
        public String getUserId() {
            return userId;
        }
        public void setUserId(String userId) {
            this.userId = userId;
        }
        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public Address getAddress() {
            return address;
        }
        public void setAddress(Address address) {
            this.address = address;
        }
        @Override
        public User clone() throws CloneNotSupportedException {
            
            User user = (User) super.clone();
            if (null != this.address) {
                user.setAddress(this.address.clone());
            }
            return user;
        }
        @Override
        public String toString() {
            return "User [userId=" + userId + ", userName=" + userName + ", address=" + address + "]";
        }
    }
    
    static class Address implements Cloneable {
        
        private String city;
        private int zipcode;
        
        public Address(String city, int zipcode) {
            this.city = city;
            this.zipcode = zipcode;
        }
        public String getCity() {
            return city;
        }
        public void setCity(String city) {
            this.city = city;
        }
        public int getZipcode() {
            return zipcode;
        }
        public void setZipcode(int zipcode) {
            this.zipcode = zipcode;
        }
        @Override
        public Address clone() throws CloneNotSupportedException {
            return (Address) super.clone();
        }
        @Override
        public String toString() {
            return "Address [city=" + city + ", zipcode=" + zipcode + "]";
        }
    }
}
