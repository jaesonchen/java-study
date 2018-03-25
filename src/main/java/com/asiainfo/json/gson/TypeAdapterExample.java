package com.asiainfo.json.gson;

import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * TypeAdapter 拥有最高优先权，设置Adapter后，类型的序列化、反序列化都只会调用TypeAdapter的方法
 * 
 * @author       zq
 * @date         2018年3月25日  下午4:53:08
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class TypeAdapterExample {

    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new TypeAdapter<User>() {//为User注册TypeAdapter
                    @Override
                    public void write(JsonWriter out, User value) throws IOException {
                        out.beginObject();
                        out.name("name").value(value.name);
                        out.name("age").value(value.age);
                        out.name("email").value(value.emailAddress);
                        out.endObject();
                    }
                    @Override
                    public User read(JsonReader in) throws IOException {
                        User user = new User();
                        in.beginObject();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "name":
                                    user.name = in.nextString();
                                    break;
                                case "age":
                                    user.age = in.nextInt();
                                    break;
                                case "email":
                                case "email_address":
                                case "emailAddress":
                                    user.emailAddress = in.nextString();
                                    break;
                            }
                        }
                        in.endObject();
                        return user;
                    }
                })
                .create();
        
        String json = gson.toJson(new User("chenzq", 24, "chenzq@163.com"));
        System.out.println(gson.fromJson(json, User.class));
        
        // gson容错
        gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new TypeAdapter<Integer>() {
                    @Override
                    public void write(JsonWriter out, Integer value) throws IOException {
                        out.value(String.valueOf(value)); 
                    }
                    @Override
                    public Integer read(JsonReader in) throws IOException {
                        try {
                            return Integer.parseInt(in.nextString());
                        } catch (NumberFormatException e) {
                            return -1;
                        }
                    }
                })
                .create();
        System.out.println(gson.toJson(100)); // 结果："100"
        System.out.println(gson.fromJson("\"\"",Integer.class)); // 结果：-1
        
        // JsonSerializer与JsonDeserializer
        gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new JsonDeserializer<Integer>() { //只接管反序列化
                    @Override
                    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        try {
                            return json.getAsInt();
                        } catch (NumberFormatException e) {
                            return -1;
                        }
                    }
                })
                .create();
        System.out.println(gson.toJson(100)); //结果：100
        System.out.println(gson.fromJson("\"\"", Integer.class)); //结果-1
        
        // @JsonAdapter(UserTypeAdapter.class) 标注在类上时，不需要GsonBuilder.registerTypeAdapter进行注册
    }
}
