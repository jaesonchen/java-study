package com.asiainfo.insidejvm;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.*;

/**
 * 
 * @Description: java io示例
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:58:24
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class IOExample {

	//跨平台的文件分割符
	static void separator() {
		System.out.println(File.separator);		// \
        System.out.println(File.pathSeparator);	// ;
	}
	//创建文件，创建成功返回true，当文件名已存在时返回false
	static boolean create(String fileName) throws IOException {
        File f = new File(fileName);
        if (!f.exists()) {
            return f.createNewFile();
        }
        return false;
	}
	//删除文件或目录，删除目录时，File代表的目录必须是空的。
	//java.nio.file.Files的delete方法，在无法删除一个文件是抛出IOException。
	static boolean delete(String fileName) {
        File f = new File(fileName);
        if (f.exists()) {
            return f.delete();
        }
        return false;
	}
	//创建目录
	static boolean mkdir(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) {
            return f.mkdir();
        }
        return false;
	}
    //判断一个指定的路径是否为目录
    static boolean isDirectory(String fileName) {
        File f = new File(fileName);
        return f.exists() && f.isDirectory();
    }
	//列出指定目录的全部文件（包括隐藏文件）
	//使用list返回的是String数组，只包含文件名。
	static List<String> list(String fileName) {
	    List<String> result = new ArrayList<>();
	    File f = new File(fileName);
	    if (f.exists()) {
	        for (String file : f.list()) {
	            result.add(file);
	        }
	    }
	    return result;
	}
	//列出指定目录的全部文件（包括隐藏文件）
	//listFiles返回的是File的数组，包含完整路径。
	static List<File> listFile(String fileName) {
	    List<File> result = new ArrayList<>();
        File f = new File(fileName);
        if (f.exists()) {
            for (File file : f.listFiles()) {
                result.add(file);
            }
        }
        return result;
	}
	//搜索指定目录的全部内容
	static List<File> listAll(File file) {
	    List<File> result = new ArrayList<>();
	    if (file.isDirectory()) {
	        for (File f : file.listFiles()) {
	            result.add(f);
	            if (f.isDirectory()) {
	                result.addAll(listAll(f));
	            }
	        }
	    }
	    return result;
	}
	//写入字节流
	static void outputStream(String fileName) throws IOException {
		OutputStream out = new FileOutputStream(new File(fileName));
		String str = "你好";
		//使用系统默认编码
		out.write(str.getBytes());
		byte[] buff = "北京".getBytes();
		for (int i = 0; i < buff.length; i++) {
			out.write(buff[i]);
		}
		out.close();
	}
	//向文件中追加新内容
	static void appendFile(String fileName) throws IOException {
		OutputStream out = new FileOutputStream(new File(fileName), true);
		String str="\r\njaesonchen";
		byte[] b = str.getBytes();
		out.write(b);
		out.close();
	}
	//读取文件内容
	static void inputStream(String fileName) throws IOException {
		InputStream in = new FileInputStream(new File(fileName));
        byte[] b = new byte[1024];
        int len = in.read(b);
        in.close();
        System.out.println("读入长度为：" + len);
        System.out.println(new String(b, 0, len));
	}
	//一个个字节读
	static void inputStream2(String fileName) throws IOException {
		InputStream in = new FileInputStream(new File(fileName));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        int b = 0;
        while ((b = in.read()) != -1) {
        	out.write(b);
        }
        System.out.println(new String(out.toByteArray()));
        in.close();
        out.close();
	}
	
	//写入字符流
	static void writer(String fileName) throws IOException {
		Writer out = new FileWriter(new File(fileName));
		//文件追加
		//Writer out = new FileWriter(new File(fileName), true);
        String str = "hello";
        out.write(str);
        out.close();
	}
	//读取字符流
	static void reader(String fileName) throws IOException {
		Reader in = new FileReader(new File(fileName));
		StringBuilder sb = new StringBuilder();
		int temp = 0;
        int i = 0;
        while ((temp = in.read()) != -1 && i < 1024) {
            sb.append((char) temp);
        }
        in.close();
        System.out.println(sb.toString());
	}
	//文件复制
	static void fileCopy(String source, String dest) throws IOException {
		InputStream in = new FileInputStream(new File(source));
        OutputStream out = new FileOutputStream(new File(dest));
        int temp=0;
        while ((temp = in.read()) != -1) {
        	out.write(temp);
        }
        in.close();
        out.close(); 
	}
	//OutputStreramWriter 和InputStreamReader
	//将字节输出流转化为字符输出流
	static void stream2writer(String fileName) throws IOException {
        Writer out = new OutputStreamWriter(new FileOutputStream(new File(fileName)));
        out.write("hello world");
        out.close();
	}
	//将字节输入流变为字符输入流
	static void stream2reader(String fileName) throws IOException {
        Reader in = new InputStreamReader(new FileInputStream(new File(fileName)));
        StringBuilder sb = new StringBuilder();
        int temp = 0;
        int i = 0;
        while ((temp = in.read()) != -1 && i < 1024) {
            sb.append((char) temp);
        }
        in.close();
        System.out.println(sb.toString());
	}
	//ByteArrayInputStream 将内容写入内存
	//ByteArrayOutputStream 将内容从内存输出
	static void byteArrayStream() throws IOException {
		String str = "ROLLENHOLT";
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("utf-8"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int temp = 0;
        while ((temp = in.read()) != -1) {
            out.write(Character.toLowerCase(temp));
        }
        System.out.println(new String(out.toByteArray()));
        in.close();
        out.close();
	}
	
	//PrintStream打印流
	static void printStream(String fileName) throws IOException {
		PrintStream print = new PrintStream(new FileOutputStream(new File(fileName)));
        print.println(true);
        print.println("hello");
        //格式化输出
        print.printf("姓名：%s  年龄：%d", "jaesonchen", 30);
        print.close();
        //向控制台输出
        OutputStream out = System.out;
        out.write("hello world".getBytes());
        out.close();
	}
	//输入输出重定向
	static void redirect(String fileName) throws IOException {		
		System.setIn(new FileInputStream(new File(fileName)));
        byte[] bytes = new byte[1024];
        int len = 0;
        len = System.in.read(bytes);
        System.out.println("读入的内容为：" + new String(bytes, 0, len));
        
        System.setOut(new PrintStream(new FileOutputStream(new File(fileName))));
        System.out.println("这些内容在文件中才能看到哦！"); 
	}
	
	//BufferedReader只能接受字符流的缓冲区
	static void bufferReader() throws IOException {
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        System.out.println("请输入内容");
        str = buf.readLine();
        System.out.println("你输入的内容是：" + str);
	}
	//数据操作流DataOutputStream、DataInputStream
	static void dataOutput(String fileName) throws IOException {
		DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(fileName)));
		out.writeChar('A');
		out.writeInt(37);
		out.writeBoolean(false);
        out.close();
	}
	static void dataInput(String fileName) throws IOException {
		DataInputStream input = new DataInputStream(new FileInputStream(new File(fileName)));
		System.out.println(input.readChar());
		System.out.println(input.readInt());
		System.out.println(input.readBoolean());
		input.close();
	}

	//文件压缩 ZipOutputStream
	static void zipOutputStream(File file, File zipFile) throws IOException {
        InputStream input = new FileInputStream(file);
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
        zipOut.putNextEntry(new ZipEntry(file.getName()));
        // 设置注释
        zipOut.setComment("hello world");
        int temp = 0;
        while ((temp = input.read()) != -1) {
            zipOut.write(temp);
        }
        input.close();
        zipOut.close();
	}
	//压缩多个文件
	static void zipOutputStream2(File file, File zipFile) throws IOException {
        InputStream input = null;
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isFile()) {
                    input = new FileInputStream(f);
                    zipOut.putNextEntry(new ZipEntry(file.getName()
                            + File.separator + f.getName()));
                    int temp = 0;
                    while ((temp = input.read()) != -1) {
                    	zipOut.write(temp);
                    }
                    input.close();
                }
            }
        }
        zipOut.close();
	}
	//解压缩
	static void zipExtract(File file, File dest) throws IOException {
		 File outFile = null;
		 ZipFile zipFile = new ZipFile(file);
		 ZipInputStream zipInput = new ZipInputStream(new FileInputStream(file));
		 ZipEntry entry = null;
		 InputStream input = null;
	     OutputStream output = null;
	     while ((entry = zipInput.getNextEntry()) != null) {
	            System.out.println("解压缩" + entry.getName() + "文件");
	            outFile = new File(dest.getAbsolutePath() + File.separator + entry.getName());
	            if (!outFile.getParentFile().exists()) {
	                outFile.getParentFile().mkdir();
	            }
	            if (!outFile.exists()) {
	                outFile.createNewFile();
	            }
	            input = zipFile.getInputStream(entry);
	            output = new FileOutputStream(outFile);
	            int temp = 0;
	            while ((temp = input.read()) != -1)	{
	            	output.write(temp);
	            }
	            input.close();
	            output.close();
	     }
	     zipInput.close();
	     zipFile.close();
	}
	//ObjectInputStream和ObjectOutputStream 对象序列化
	static void objectOutputStream(File file) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(new Student("jaeson", 30));
        oos.close();
	}
	static void objectInputStream(File file) throws Exception {
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
        Object obj = input.readObject();
        input.close();
        System.out.println(obj);
	}
	
	public static void main(String[] args) throws Exception {

	}
}

//序列化
class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    public Student() {}
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
    @Override public String toString() {
        return "姓名：" + name + "  年龄：" + age;
    }
    //transient成员不会被序列化
    private transient String name;
    private int age;
}
