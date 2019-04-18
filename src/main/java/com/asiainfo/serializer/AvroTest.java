package com.asiainfo.serializer;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

/**
 * eclipse maven lifecycle-mapping-metadata.xml增加配置：
 * 
<?xml version="1.0" encoding="UTF-8"?>
<lifecycleMappingMetadata>
  <pluginExecutions>
    <pluginExecution>
      <pluginExecutionFilter>
        <groupId>org.apache.avro</groupId>
        <artifactId>avro-maven-plugin</artifactId>
        <versionRange>1.8.2</versionRange>
        <goals>
          <goal>schema</goal>
        </goals>
      </pluginExecutionFilter>
      <action>
        <ignore />
      </action>
    </pluginExecution>
  </pluginExecutions>
</lifecycleMappingMetadata>
 * 
 * @author       zq
 * @date         2018年1月22日  下午2:33:40
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class AvroTest {

    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) throws IOException {

        avroSerializer();
        avroDeserializer();
        avroWithoutCodeGenerate();
    }

    static void avroSerializer() throws IOException {
        
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);
        // Leave favorite color null
        
        // Alternate constructor
        User user2 = new User("Ben", 7, "red");

        // Construct via builder
        User user3 = User.newBuilder()
                     .setName("Charlie")
                     .setFavoriteColor("blue")
                     .setFavoriteNumber(null)
                     .build();
        
        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
        DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
        dataFileWriter.create(user1.getSchema(), new File("user.avro"));
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.append(user3);
        dataFileWriter.close();
    }
    
    static void avroDeserializer() throws IOException {

        // Deserialize Users from disk
        DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
        DataFileReader<User> dataFileReader = new DataFileReader<User>(new File("user.avro"), userDatumReader);
        User user = null;
        while (dataFileReader.hasNext()) {
            // Reuse user object by passing it to next(). This saves us from
            // allocating and garbage collecting many objects for files with
            // many items.
            user = dataFileReader.next(user);
            System.out.println(user);
        }
        dataFileReader.close();
    }
    
    static void avroWithoutCodeGenerate() throws IOException {
        
        Schema schema = new Schema.Parser().parse(new File("src/main/avro/user.avsc"));
        
        GenericRecord user1 = new GenericData.Record(schema);
        user1.put("name", "jaeson");
        user1.put("favorite_number", 256);
        // Leave favorite color null

        GenericRecord user2 = new GenericData.Record(schema);
        user2.put("name", "chenzq");
        user2.put("favorite_number", 7);
        user2.put("favorite_color", "red");
        
        // Serialize user1 and user2 to disk
        File file = new File("users_without.avro");
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
        dataFileWriter.create(schema, file);
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.close();
        
        // Deserialize users from disk
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>();
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader);
        GenericRecord user = null;
        while (dataFileReader.hasNext()) {
            // Reuse user object by passing it to next(). This saves us from
            // allocating and garbage collecting many objects for files with
            // many items.
            user = dataFileReader.next(user);
            System.out.println(user);
        }
        dataFileReader.close();
    }
}
