/**
 * @Title:  IOUtilsExample.java
 * @Package: com.asiainfo.fileservice
 * @Description: TODO
 * @author: chenzq
 * @date:   2019年2月5日 下午8:46:18
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
package com.asiainfo.apacheutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.output.StringBuilderWriter;

/**   
 * @Description: apache IOUtils 的使用
 * @author chenzq  
 * @date 2019年2月5日 下午8:46:18
 * @version V1.0  
 */
public class ApacheIOUtils {

    public static final String LINE_SEPARATOR;
    // 跨平台换行符
    static {
        final StringBuilderWriter buf = new StringBuilderWriter(4);
        final PrintWriter out = new PrintWriter(buf);
        out.println();
        LINE_SEPARATOR = buf.toString();
        out.close();
    }

    /**
     * @Description: TODO
     * @author chenzq
     * @date 2019年3月19日 下午4:17:17
     * @param args
     */
    public static void main(String[] args) {

        lineIterator();
    }

    // 优雅的socket、流关闭方法
    public static void closeQuietly() {
        
        char[] data = new char[1024];
        Reader in = null;
        try {
            in = new FileReader("src/main/java/com/asiainfo/apacheutils/foo.txt");
            in.read(data);
            in.close(); //close errors are handled
        } catch (Exception e) {
            // error handling
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
    
    // 输入流转字符串
    public static void inputStreamToString() {
        
        InputStream in = null;
        try {
            in = new FileInputStream(new File("src/main/java/com/asiainfo/apacheutils/foo.txt"));
            String content = IOUtils.toString(in, Charset.forName("utf-8"));
            System.out.println(content);
            in.close();
        } catch (Exception e) {
            // error handling
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
    
    // 流拷贝
    public static void streamCopy() {
        
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(new File("src/main/java/com/asiainfo/apacheutils/foo.txt"));
            out = new FileOutputStream(new File("src/main/java/com/asiainfo/apacheutils/foo_copy.txt"));
            IOUtils.copy(in, out);
            in.close();
            out.close();
        } catch (Exception e) {
            // error handling
        } finally {
            IOUtils.closeQuietly(in, out);
        }
    }
    
    // ToByteArray
    public static void toByteArray() {
        
        InputStream in = null;
        try {
            in = new FileInputStream(new File("src/main/java/com/asiainfo/apacheutils/foo.txt"));
            byte[] bArray = IOUtils.toByteArray(in);
            System.out.println(bytesToHexString(bArray));
            in.close();
        } catch (Exception e) {
            // error handling
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
    
    // ToInputStream
    public static void toInputStream() {
        
        InputStream in = null;
        try {
            String input = "hello wolrd!";
            in = IOUtils.toInputStream(input, "utf-8");
            byte[] buff = new byte[1024];
            in.read(buff);
            System.out.println(new String(buff, "utf-8"));
            in.close();
        } catch (Exception e) {
            // error handling
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
    
    // readLines
    public static void readLines() {
        
        InputStream in = null;
        try {
            in = new FileInputStream(new File("src/main/java/com/asiainfo/apacheutils/foo.txt"));
            List<String> list = IOUtils.readLines(in, "utf-8");
            System.out.println(list);
            in.close();
        } catch (Exception e) {
            // error handling
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
    
    // writeLines
    public static void writeLines() {
        
        OutputStream out = null;
        try {
            List<String> list = Arrays.asList(new String[] {"hello world", "how are you", "welcome"});
            out = new FileOutputStream(new File("src/main/java/com/asiainfo/apacheutils/foo_copy.txt"));
            IOUtils.writeLines(list, LINE_SEPARATOR, out, "utf-8");
            out.close();
        } catch (Exception e) {
            // error handling
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
    
    // LineIterator
    public static void lineIterator() {
        
        InputStream in = null;
        try {
            in = new FileInputStream(new File("src/main/java/com/asiainfo/apacheutils/foo_copy.txt"));
            LineIterator it = IOUtils.lineIterator(in, "utf-8");
            while (it.hasNext()) {
                String line = it.nextLine();
                System.out.println(line);
            }
            in.close();
        } catch (Exception e) {
            // error handling
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
    
    // 字节转16进制字符
    private static String bytesToHexString(byte[] bArray) {
        
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
}
