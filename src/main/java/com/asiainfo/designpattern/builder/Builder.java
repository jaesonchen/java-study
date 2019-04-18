package com.asiainfo.designpattern.builder;

import java.io.Serializable;

/**
 * 遇到多个构造器参数时要考虑使用构建器模式（Builder模式）：
 * 构造器和静态工厂方法有个共同的局限性：它们都不能很好的扩张到大量的可选参数。
 * 通常使用JavaBean来包装参数，缺点是在构造过程中JavaBean可能出于不一致的状态。类无法仅仅通过检验构造器参数的有效性来保证一致性。
 * 与传统的重叠构造器相比，使用Builder模式的客户端将更易于阅读和编写，构造器也比JavaBean更安全。
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:33:17
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Builder {

    public static void main(String[] args) {
        
        User user = new User.UserBuilder("1001")
                .firstName("chen")
                .lastName("zq")
                .address("bj")
                .sex(1)
                .age(20)
                .zipcode(10010)
                .status(0)
                .build();
        System.out.println(user);
    }

    public static class User implements Serializable {

        /** serialVersionUID */
        private static final long serialVersionUID = 1L;
        
        private String userId;
        private String firstName;
        private String lastName;
        private String address;
        private int sex;
        private int age;
        private int zipcode;
        private int status;
        
        public User() {}
        public User(String userId, String firstName, String lastName, String address, 
                int sex, int age, int zipcode, int status) {
            this.userId = userId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.sex = sex;
            this.age = age;
            this.zipcode = zipcode;
            this.status = status;
        }
        private User(UserBuilder builder) {
            this(builder.userId, builder.firstName, builder.lastName, builder.address, 
                    builder.sex, builder.age, builder.zipcode, builder.status);
        }
        //builder
        public static class UserBuilder {
            
            private String userId;
            private String firstName;
            private String lastName;
            private String address;
            private int sex;
            private int age;
            private int zipcode;
            private int status;
            public UserBuilder() {}
            public UserBuilder(String userId) {
                this.userId = userId;
            }
            
            public User build() {
                return new User(this);
            }
            public UserBuilder userId(String userId) {
                this.userId = userId;
                return this;
            }
            public UserBuilder firstName(String firstName) {
                this.firstName = firstName;
                return this;
            }
            public UserBuilder lastName(String lastName) {
                this.lastName = lastName;
                return this;
            }
            public UserBuilder address(String address) {
                this.address = address;
                return this;
            }
            public UserBuilder sex(int sex) {
                this.sex = sex;
                return this;
            }
            public UserBuilder age(int age) {
                this.age = age;
                return this;
            }
            public UserBuilder zipcode(int zipcode) {
                this.zipcode = zipcode;
                return this;
            }
            public UserBuilder status(int status) {
                this.status = status;
                return this;
            }
        }
        public String getUserId() {
            return userId;
        }
        public void setUserId(String userId) {
            this.userId = userId;
        }
        public String getFirstName() {
            return firstName;
        }
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        public String getLastName() {
            return lastName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }
        public int getSex() {
            return sex;
        }
        public void setSex(int sex) {
            this.sex = sex;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
        public int getZipcode() {
            return zipcode;
        }
        public void setZipcode(int zipcode) {
            this.zipcode = zipcode;
        }
        public int getStatus() {
            return status;
        }
        public void setStatus(int status) {
            this.status = status;
        }
        @Override
        public String toString() {
            return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", address="
                    + address + ", sex=" + sex + ", age=" + age + ", zipcode=" + zipcode + ", status=" + status + "]";
        }
    }
}
