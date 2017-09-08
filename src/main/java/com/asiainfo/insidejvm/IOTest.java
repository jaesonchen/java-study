package com.asiainfo.insidejvm;

import java.io.*;
import java.util.zip.*;

public class IOTest {

	//跨平台的文件分割符
	static void separator() {
		System.out.println(File.separator);		// \
        System.out.println(File.pathSeparator);	// ;
	}
	//创建文件，创建成功返回true，当文件名已存在时返回false
	static void create() throws IOException {
		String fileName = "C:" + File.separator + "hello" + File.separator + "hello.txt";
        File f = new File(fileName);
        f.createNewFile();
	}
	//删除文件或目录，删除目录时，File代表的目录必须是空的。
	//java.nio.file.Files的delete方法，在无法删除一个文件是抛出IOException。
	static void delete() {
		String fileName = "C:" + File.separator + "hello" + File.separator + "hello.txt";
        File f = new File(fileName);
        if (f.exists()) {
            f.delete();
        } else {
            System.out.println("文件不存在");
        }
	}
	//创建目录
	static void mkdir() {
		String fileName = "C:" + File.separator + "hello";
        File f = new File(fileName);
        f.mkdir();
	}
	//列出指定目录的全部文件（包括隐藏文件）
	//使用list返回的是String数组，只包含文件名。
	static void list() {
		String fileName = "C:" + File.separator + "hello";
	    File f = new File(fileName);
	    String[] str = f.list();
	    for (int i = 0; i < str.length; i++) {
	        System.out.println(str[i]);
	    }
	}
	//列出指定目录的全部文件（包括隐藏文件）
	//listFiles返回的是File的数组，包含完整路径。
	static void listFile() {
		String fileName = "C:" + File.separator + "hello";
        File f = new File(fileName);
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i]);
        }
	}
	//判断一个指定的路径是否为目录
	static void isDirectory() {
		String fileName = "C:" + File.separator + "hello";
        File f = new File(fileName);
        if (f.isDirectory()) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
	}
	//搜索指定目录的全部内容
	static void listAll() {
		String fileName = "C:" + File.separator + "hello";
        File f = new File(fileName);
        print(f);
	}
	private static void print(File f) {
		if (f != null) {
            if (f.isDirectory()) {
                File[] fileArray = f.listFiles();
                if (fileArray != null)
                    for (int i = 0; i < fileArray.length; i++)
                        print(fileArray[i]);

            } else {
                System.out.println(f);
            }
		}
	}
	//写入字节流
	static void outputStream() throws IOException {
		String fileName = "C:" + File.separator + "hello" + File.separator + "hello.txt";
		OutputStream out = new FileOutputStream(new File(fileName));
		String str = "你好";
		//使用系统默认编码
		byte[] b = str.getBytes();
		out.write(b);
		b = "北京".getBytes();
		for (int i = 0; i < b.length; i++) {
			out.write(b[i]);
		}
		out.close();
	}
	//向文件中追加新内容
	static void appendFile() throws IOException {
		String fileName = "C:" + File.separator + "hello" + File.separator + "hello.txt";
		OutputStream out = new FileOutputStream(new File(fileName), true);
		String str="\r\njaesonchen";
		byte[] b = str.getBytes();
		out.write(b);
		out.close();
	}
	//读取文件内容
	static void inputStream() throws IOException {
		String fileName = "C:" + File.separator + "hello" + File.separator + "hello.txt";
		InputStream in = new FileInputStream(new File(fileName));
        byte[] b = new byte[1024];
        int len = in.read(b);
        in.close();
        System.out.println("读入长度为：" + len);
        System.out.println(new String(b, 0, len));
	}
	//一个个字节读
	static void inputStream2() throws IOException {
		String fileName = "C:" + File.separator + "hello" + File.separator + "hello.txt";
		InputStream in = new FileInputStream(new File(fileName));
        byte[] b = new byte[1024];
        int temp = 0;
        int i = 0;
        while ((temp = in.read()) != -1 && i < 1024) {
        	b[i++] = (byte) temp;
        }
        in.close();
        System.out.println(new String(b, 0, i));
	}
	
	//写入字符流
	static void writer() throws IOException {
		String fileName = "C:" + File.separator + "hello" + File.separator + "hello_writer.txt";
		Writer out = new FileWriter(new File(fileName));
		//文件追加
		//Writer out = new FileWriter(new File(fileName), true);
        String str = "hello";
        out.write(str);
        out.close();
	}
	//读取字符流
	static void reader() throws IOException {
		String fileName = "C:" + File.separator + "hello" + File.separator + "hello_writer.txt";
		Reader in = new FileReader(new File(fileName));
		char[] ch = new char[1024];
		int temp = 0;
        int i = 0;
        while((temp = in.read()) != -1 && i < 1024)
            ch[i++] = (char) temp;
        in.close();
        System.out.println(new String(ch, 0, i));
	}
	//文件复制
	static void fileCopy() throws IOException {
		String fileName1 = "C:" + File.separator + "hello" + File.separator + "hello.txt";
		String fileName2 = "C:" + File.separator + "hello" + File.separator + "hello_copy.txt";
		InputStream in = new FileInputStream(new File(fileName1));
        OutputStream out = new FileOutputStream(new File(fileName2));
        int temp=0;
        while((temp = in.read()) != -1)
        	out.write(temp);
        in.close();
        out.close(); 
	}
	//OutputStreramWriter 和InputStreamReader
	//将字节输出流转化为字符输出流
	static void stream2writer() throws IOException {
		String fileName = "C:" + File.separator + "hello" + File.separator + "helloworld.txt";
        Writer out = new OutputStreamWriter(new FileOutputStream(new File(fileName)));
        out.write("hello world");
        out.close();
	}
	//将字节输入流变为字符输入流
	static void stream2reader() throws IOException {
		String fileName = "C:" + File.separator + "hello" + File.separator + "helloworld.txt";
        Reader in = new InputStreamReader(new FileInputStream(new File(fileName)));
        char[] ch = new char[1024];
        int temp = 0;
        int i = 0;
        while((temp = in.read()) != -1 && i < 1024)
            ch[i++] = (char) temp;
        in.close();
        System.out.println(new String(ch, 0, i));
	}
	//ByteArrayInputStream 将内容写入内存
	//ByteArrayOutputStream 将内容从内存输出
	static void byteArrayStream() throws IOException {
		String str="ROLLENHOLT";
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int temp = 0;
        while((temp = in.read()) != -1){
            char ch = (char) temp;
            out.write(Character.toLowerCase(ch));
        }
        String outStr = out.toString();
        in.close();
        out.close();
        System.out.println(outStr);
	}
	
	//PrintStream打印流
	static void printStream() throws IOException {
		String fileName = "C:" + File.separator + "hello" + File.separator + "printstream.txt";
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
	static void redirect() throws IOException {
		String fileName = "C:" + File.separator + "hello" + File.separator + "printstream.txt";
		
		System.setIn(new FileInputStream(new File(fileName)));
        byte[] bytes = new byte[1024];
        int len = 0;
        len = System.in.read(bytes);
        System.out.println("读入的内容为：" + new String(bytes, 0, len));
        
        fileName = "C:" + File.separator + "hello" + File.separator + "redirect.txt";
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
	static void dataOutput() throws IOException {
		String fileName = "C:" + File.separator + "hello" + File.separator + "dataoutput.txt";
		DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(fileName)));
		out.writeChar('A');
		out.writeInt(37);
		out.writeBoolean(false);
        out.close();
	}
	static void dataInput() throws IOException {
		String fileName = "C:" + File.separator + "hello" + File.separator + "dataoutput.txt";
		DataInputStream input = new DataInputStream(new FileInputStream(new File(fileName)));
		System.out.println(input.readChar());
		System.out.println(input.readInt());
		System.out.println(input.readBoolean());
		input.close();
	}
	//SequenceInputStream 合并流 
	static void sequenceStream() throws IOException {
		File file1 = new File("C:" + File.separator + "hello" + File.separator + "dataoutput.txt");
        File file2 = new File("C:" + File.separator + "hello" + File.separator + "redirect.txt");
        File file3 = new File("C:" + File.separator + "hello" + File.separator + "sequence.txt");
        InputStream input1 = new FileInputStream(file1);
        InputStream input2 = new FileInputStream(file2);
        OutputStream output = new FileOutputStream(file3);
        // 合并流
        SequenceInputStream sis = new SequenceInputStream(input1, input2);
        int temp = 0;
        while ((temp = sis.read()) != -1)
            output.write(temp);
        input1.close();
        input2.close();
        output.close();
        sis.close();
	}
	//文件压缩 ZipOutputStream
	static void zipOutputStream() throws IOException {
		File file = new File("C:" + File.separator + "hello" + File.separator + "hello.txt");
        File zipFile = new File("C:" + File.separator + "hello" + File.separator + "hello.zip");
        InputStream input = new FileInputStream(file);
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
        zipOut.putNextEntry(new ZipEntry(file.getName()));
        // 设置注释
        zipOut.setComment("hello world");
        int temp = 0;
        while ((temp = input.read()) != -1)
            zipOut.write(temp);
        input.close();
        zipOut.close();
	}
	//压缩多个文件
	static void zipOutputStream2() throws IOException {
		// 要被压缩的文件夹
        File file = new File("C:" + File.separator + "hello" + File.separator + "test");
        File zipFile = new File("C:" + File.separator + "hello" + File.separator + "hello.zip");
        InputStream input = null;
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length && !files[i].isDirectory(); i++) {
                input = new FileInputStream(files[i]);
                zipOut.putNextEntry(new ZipEntry(file.getName()
                        + File.separator + files[i].getName()));
                int temp = 0;
                while ((temp = input.read()) != -1)
                	zipOut.write(temp);
                input.close();
            }
        }
        zipOut.close();
	}
	//解压缩
	static void zipExtract() throws IOException {
		 File file = new File("C:" + File.separator + "hello" + File.separator + "hello.zip");
		 File outFile = null;
		 ZipFile zipFile = new ZipFile(file);
		 ZipInputStream zipInput = new ZipInputStream(
				 new FileInputStream(file));
		 ZipEntry entry = null;
		 InputStream input = null;
	     OutputStream output = null;
	     while ((entry = zipInput.getNextEntry()) != null) {
	            System.out.println("解压缩" + entry.getName() + "文件");
	            outFile = new File("C:" + File.separator + "hello" 
	            		+ File.separator + "extract" + File.separator + entry.getName());

	            if (!outFile.getParentFile().exists())
	                outFile.getParentFile().mkdir();
	            if (!outFile.exists())
	                outFile.createNewFile();
	            input = zipFile.getInputStream(entry);
	            output = new FileOutputStream(outFile);
	            int temp = 0;
	            while ((temp = input.read()) != -1)	
	            	output.write(temp);
	            input.close();
	            output.close();
	     }
	     zipInput.close();
	     zipFile.close();
	}
	//ObjectInputStream和ObjectOutputStream 对象序列化
	static void objectOutputStream() throws IOException {
		File file = new File("C:" + File.separator + "hello" + File.separator + "object.txt");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(new Student("jaeson", 30));
        oos.close();
	}
	static void objectInputStream() throws Exception {
		File file = new File("C:" + File.separator + "hello" + File.separator + "object.txt");
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
        Object obj = input.readObject();
        input.close();
        System.out.println(obj);
	}
	
	public static void main(String[] args) throws Exception {
		//objectInputStream();
		//objectOutputStream();
		//zipExtract();
		//zipOutputStream2();
		//zipOutputStream();
		//sequenceStream();
		//dataInput();
		//bufferReader();
		//redirect();
		//printStream();
		//byteArrayStream();
		//stream2reader();
		//stream2writer();
		//fileCopy();
		//reader();
		//writer();
		//inputStream2();
		//inputStream();
		//appendFile();
		//outputStream();
		//separator();
		//create();
		//delete();
		//mkdir();
		//list();
		//listFile();
		//isDirectory();
		//listAll();
		/*Send send = new Send();
        Recive recive = new Recive();
        //管道连接
        send.getOut().connect(recive.getInput());
        new Thread(send).start();
        new Thread(recive).start();*/
	}
}
//PipedOutputStream 管道输出流
//PipedInputStream 管道输入流
//发送消息类
class Send implements Runnable {
    private PipedOutputStream out = null;
    public Send() {
        out = new PipedOutputStream();
    }
    public PipedOutputStream getOut() {
        return this.out;
    }
    @Override public void run() {
        String message = "hello jaesonchen";
        try {
            out.write(message.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//接受消息类
class Recive implements Runnable {
    private PipedInputStream input = null;
    public Recive() {
        this.input = new PipedInputStream();
    }
    public PipedInputStream getInput() {
        return this.input;
    }
    @Override public void run() {
        byte[] b=new byte[1024];
        int len=0;
        try {
            len = this.input.read(b);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("接受的内容为 " + new String(b, 0, len));
    }
}
//Serializable
@SuppressWarnings("all")
class Student implements Serializable{
    public Student() { }
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
