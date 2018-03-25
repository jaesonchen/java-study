package com.asiainfo.json.gson;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * 1. 无法序列化循环引用的对象。
 * 2. 对象的field最好使用基本数据类型。
 * 3. 默认类的所有field、超类的field都会序列化、反序列化，不需要使用annotation标识。
 * 4. 声明为transient的field不会被序列化、反序列化。
 * 5. gson可以正确处理null。
 * 6. 序列化时，null被忽略；反序列化时，field没有对应的entry时，object被赋值为null，primitive被赋值为默认值。
 * 7. 由于java 泛型的erasure，反序列化时需要使用TypeToken指定泛型参数类型，否则反序列化为LinkedTreeMap。
 * 8. 要改变Gson的默认设置，需要使用GsonBuilder构建Gson。
 * 
 * @author       zq
 * @date         2018年3月24日  下午3:52:00
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class GsonExample {

    /** 
     * TODO
     * 
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {

        System.out.println("==================primitive====================");
        primitive();
        System.out.println("==================array====================");
        array();
        System.out.println("==================collection====================");
        collection();
        System.out.println("==================object====================");
        object();
        System.out.println("==================annotation====================");
        annotation();
        System.out.println("==================stream====================");
        stream();
        System.out.println("==================GsonBuilder====================");
        gsonBuilder();
    }

    // gson < -- > primitive
    public static void primitive() {
        
        Gson gson = new Gson();
        // Serialization
        System.out.println(gson.toJson(1));                     // ==> 1
        System.out.println(gson.toJson(new Long(10)));          // ==> 10
        System.out.println(gson.toJson(new Double(9.9)));       // ==> 9.9
        System.out.println(gson.toJson(false));                 // ==> false
        System.out.println(gson.toJson("abcd"));                // ==> "abcd"

        // Deserialization
        System.out.println(gson.fromJson("1", int.class));
        System.out.println(gson.fromJson("1", Integer.class));
        System.out.println(gson.fromJson("10", Long.class));
        System.out.println(gson.fromJson("9.9", double.class));
        System.out.println(gson.fromJson("false", Boolean.class));
        System.out.println(gson.fromJson("\"abcd\"", String.class));

    }
    
    // gson < -- > array
    public static void array() {
        
        Gson gson = new Gson();
        // Serialization
        System.out.println(gson.toJson(new int[] { 1, 2, 3 }));                         // ==> [1,2,3]
        System.out.println(gson.toJson(new String[] { "abc", "def", "gji" }));          // ==> ["abc","def","gji"]
        
        // Deserialization
        System.out.println(Arrays.asList(gson.fromJson("[1, 2, 3]", Integer[].class)));
        System.out.println(Arrays.asList(gson.fromJson("[\"abc\", \"def\", \"gji\"]", String[].class)));
        System.out.println(gson.fromJson("[\"abc\", \"cba\"]", List.class));
    }
    
    // gson < -- > collection
    // 泛型由于java的类型erasure，在反序列化时需要使用TypeToken指定泛型类型参数，否则反序列化失败。
    public static void collection() {
        
        Gson gson = new Gson();
        // Serialization
        System.out.println(gson.toJson(Lists.newArrayList(1, 2, 3, 4, 5))); // ==> [1,2,3,4,5]
        System.out.println(gson.toJson(Lists.newArrayList("abc", "cba")));  // ==> ["abc","cba"]
        
        // Deserialization
        // 继承TypeToken，而不是直接new TypeToken()的原因是TypeToken的构造函数是protected的，不可见
        Type collectionType = new TypeToken<Collection<Integer>>(){}.getType();
        Collection<Integer> intCollection = gson.fromJson("[1, 2, 3, 4, 5]", collectionType);
        System.out.println(intCollection);
    }
    
    // gson < -- > object
    // 泛型由于java的类型erasure，在反序列化时需要使用TypeToken指定泛型类型参数，否则反序列化失败。
    public static void object() {
        
        Gson gson = new Gson();
        // Serialization
        System.out.println(gson.toJson(new Bar()));
        System.out.println(gson.toJson(new Foo<Bar>(new Bar())));
        
        // Deserialization
        System.out.println(gson.fromJson("{\"age\":20,\"sex\":\"MALE\"}", Bar.class));
        // 类型参数反序列化错误: Foo [name=chenzq, t={age=20.0, sex=MALE}]
        System.out.println(gson.fromJson("{\"name\":\"chenzq\",\"t\":{\"age\":20,\"sex\":\"MALE\"}}", Foo.class));
        // com.google.gson.internal.LinkedTreeMap
        System.out.println(gson.fromJson("{\"name\":\"chenzq\",\"t\":{\"age\":20,\"sex\":\"MALE\"}}", Foo.class).getType());
        // 使用TypeToken传递类型参数
        Type FooType = new TypeToken<Foo<Bar>>(){}.getType();
        Foo<Bar> foo = gson.fromJson("{\"name\":\"chenzq\",\"t\":{\"age\":20,\"sex\":\"MALE\"}}", FooType);
        System.out.println(foo);
    }
    
    // @SerializedName 指定序列化、反序列化属性名称
    public static void annotation() {
        
        Gson gson = new Gson();
        // Serialization
        System.out.println(gson.toJson(new User("jaeson", 24, "jasonchen@163.com")));
        
        // Deserialization
        System.out.println(gson.fromJson("{\"name\":\"jaeson\",\"email\":\"jasonchen@163.com\"}", User.class));
    }
    
    // gson stream
    public static void stream() throws IOException {
        
        Gson gson = new Gson();
        String json = "{\"name\":\"jaeson\", \"age\":24, \"email\":\"jasonchen@163.com\"}";
        System.out.println(gson.fromJson(new StringReader(json), User.class));

        User user = new User();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.beginObject(); // throws IOException
        while (reader.hasNext()) {
            String s = reader.nextName();
            switch (s) {
                case "name":
                    user.name = reader.nextString();
                    break;
                case "age":
                    user.age = reader.nextInt();
                    break;
                case "email_address":
                case "emailAddress":
                case "email":
                    user.emailAddress = reader.nextString();
                    break;
                default:
                    // do nothing
            }
        }
        reader.endObject(); // throws IOException
        reader.close();
        System.out.println(user);
        
        // PrintStream(System.out) 、StringBuilder和*Writer都实现了Appendable接口。
        gson.toJson(user, System.out);
        System.out.println();
        
        @SuppressWarnings("resource")
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(System.out));
        writer.beginObject() // throws IOException
                .name("name").value("jaeson")
                .name("age").value(24)
                .name("email").nullValue()
                .endObject(); // throws IOException
        writer.flush();
        System.out.println();
    }
    
    // gson默认不输出null，要改变设置需要使用GsonBuilder构建Gson
    // @Expose必须和GsonBuilder配合使用才有效。
    public static void gsonBuilder() {
        
        Gson gson = new GsonBuilder()
                .serializeNulls()           //序列化null
                .setDateFormat("yyyy-MM-dd")// 设置日期时间格式，另有2个重载方法， 在序列化和反序化时均生效
                .disableInnerClassSerialization()   // 禁此序列化内部类
                .disableHtmlEscaping()      //禁止转义html标签
                .setPrettyPrinting()        //格式化输出
                .create();
        gson.toJson(new User("chenzq", 24, null), System.out);
        System.out.println();
        
        // @Expose
        Gson exposeGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        exposeGson.toJson(new User("chenzq", 24, "chenzq@163.com"), System.out);
        System.out.println();
        
        // modifire
        Gson modifireGson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL)
                .create();
        modifireGson.toJson(new User("chenzq", 24, "chenzq@163.com"), System.out);
        System.out.println();
    }
}

class User {
    
    final String zipCode = "10100";
    transient Sex sex = Sex.MALE;
    @Expose
    String name;
    @Expose
    int age;
    @SerializedName(value = "email_address", alternate = {"emailAddress", "email"})
    String emailAddress;
    User() {}
    User(String name, int age, String emailAddress) {
        this.name = name;
        this.age = age;
        this.emailAddress = emailAddress;
    }
    @Override
    public String toString() {
        return "User [zipCode=" + zipCode + ", sex=" + sex + ", name=" + name + ", age=" + age + ", emailAddress="
                + emailAddress + "]";
    }
}

enum Sex {
    MALE, FEMALE
}
class Bar {
    int age = 20;
    Sex sex = Sex.MALE;
    @Override
    public String toString() {
        return "Bar [age=" + age + ", sex=" + sex + "]";
    }
}
class Foo<T> {
    String name ="chen";
    T t;
    Foo(T t) {
        this.t = t;
    }
    Class<?> getType() {
        return t.getClass();
    }
    @Override
    public String toString() {
        return "Foo [name=" + name + ", t=" + t + "]";
    }
}