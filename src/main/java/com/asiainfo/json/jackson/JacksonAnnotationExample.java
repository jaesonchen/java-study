package com.asiainfo.json.jackson;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @JsonIgnore 此注解用于属性上，作用是进行JSON操作时忽略该属性。
 * 
 * @JsonFormat 此注解用于属性上，作用是把Date类型直接转化为想要的格式，如@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")。
 * 
 * @JsonProperty 此注解用于属性上，作用是把该属性的名称序列化为另外一个名称。
 * 
 * @author       zq
 * @date         2018年3月25日  下午5:45:34
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class JacksonAnnotationExample {

    /** 
     * TODO
     * 
     * @param args
     * @throws JsonProcessingException 
     */
    public static void main(String[] args) throws JsonProcessingException {
        
        Student student = new Student("chenzq", 24, Gender.MALE);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(student);
        System.out.println("json=" + json);

    }
}

class Student {
    
    String name;
    @JsonIgnore
    int age;
    @JsonProperty("sex")
    Gender gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date birthday;
    @JsonFormat(pattern = "yyyy年MM月dd日")
    Date today;

    Student() {}
    Student(String name, int age, Gender gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.birthday = new Date();
        this.today = new Date();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public Date getToday() {
        return today;
    }
    public void setToday(Date today) {
        this.today = today;
    }
    @Override
    public String toString() {
        return "Student [name=" + name + ", age=" + age + ", gender=" + gender + ", birthday=" + birthday + ", today="
                + today + "]";
    }
}