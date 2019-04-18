package com.asiainfo.clazz;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * 
 * @Description: 解析class文件在加载后的各个常量池项
 * 
 * @author       zq
 * @date         2017年10月16日  下午4:32:12
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ResolveClassFile {

    public static void main(String[] args) {
        
        File file = new File("target/classes/com/asiainfo/clazz/TestClass.class");
        System.out.println(file.getAbsolutePath() + ", " +  file.length());
        resolveClassFile(file);
    }
    
	public static void resolveClassFile(File file) {
		
		if (file.exists()) {
			try {
				FileInputStream in = new FileInputStream(file);
				DataInputStream dis = new DataInputStream(in);

				//Extract Magic number
				byte[] itemBuf = new byte[4];
				dis.read(itemBuf, 0, 4);
				String magic = bytesToHexString(itemBuf, 4);
				System.out.println("Magic:" + magic);

				//Extract minor version and major version
				dis.read(itemBuf, 0, 2);
				String minorVersion = bytesToHexString(itemBuf, 2);
				System.out.println("Minor version:" + minorVersion);
		
				dis.read(itemBuf,0,2);
				String majorVersion = bytesToHexString(itemBuf, 2);
				int mVersion = bytesToInt(itemBuf, 2);
				System.out.println("Major version:" + majorVersion + " int:" + mVersion);
		
				//Resolve Constant pool
				dis.read(itemBuf, 0, 2);
				String poolCount = bytesToHexString(itemBuf, 2);
				int constantPoolCount =bytesToInt(itemBuf, 2);
				System.out.println("Constant pool count:" + poolCount + " " + constantPoolCount);
		
				ArrayList<ConstantPoolType> pool = new ArrayList<ConstantPoolType>();
				for (int i = 0; i < constantPoolCount-1; i++) {
					dis.read(itemBuf, 0, 1);
					int tag = bytesToInt(itemBuf, 1);
					switch (tag) {
						case 1:
							String strVal = resolveCPUtf8(itemBuf, dis, pool);
							printConstantPoolVal(i, "utf8", strVal);
							break;
						case 3:
							dis.read(itemBuf, 0, 4);
							int intVal=bytesToInt(itemBuf,4);
							CONSTANT_Integer_Info integer = new CONSTANT_Integer_Info();
							integer.setValue(intVal);
							pool.add(integer);
							printConstantPoolVal(i, "int", intVal + "");
							break;
						case 4:
							dis.read(itemBuf, 0, 4);
							int floatVal=bytesToInt(itemBuf,4);
							CONSTANT_Float_Info floatValue = new CONSTANT_Float_Info();
							floatValue.setValue(floatVal);
							pool.add(floatValue);
							printConstantPoolVal(i, "float", floatVal + "");
							break;
						case 5:
							dis.read(itemBuf, 0, 8);
							long longVal = bytesToLong(itemBuf);
							CONSTANT_Long_Info longValue = new CONSTANT_Long_Info();
							longValue.setValue(longVal);
							pool.add(longValue);
							printConstantPoolVal(i, "long", longVal + "");
							break;
						case 6:
							dis.read(itemBuf, 0, 8);
							double doubleVal = bytesToDouble(itemBuf);
							CONSTANT_Double_Info doubleValue = new CONSTANT_Double_Info();
							doubleValue.setValue(doubleVal);
							pool.add(doubleValue);
							printConstantPoolVal(i, "double", doubleVal + "");
							break;
						case 7:
							dis.read(itemBuf, 0, 2);
							int classIndex = bytesToInt(itemBuf, 2);
							CONSTANT_Class_Info classinfo = new CONSTANT_Class_Info();
							classinfo.setIndex(classIndex);
							pool.add(classinfo);
							printConstantPoolVal(i, "class_index", classIndex + "");
							break;
						case 8:
							dis.read(itemBuf, 0, 2);
							int strIndex = bytesToInt(itemBuf, 2);
							CONSTANT_String_Info strindex = new CONSTANT_String_Info();
							strindex.setIndex(strIndex);
							pool.add(strindex);
							printConstantPoolVal(i, "string_index", strIndex + "");
							break;
						case 9:
							int[] fieldIndexes = getTwoIndexes(itemBuf, dis);
							CONSTANT_Fieldref_Info fieldRefValue = new CONSTANT_Fieldref_Info(fieldIndexes[0], fieldIndexes[1]);
							pool.add(fieldRefValue);
							printConstantPoolVal(i, "field_indexes", fieldIndexes[0] + " " + fieldIndexes[1]);
							break;
						case 10:
							int[] methodIndexes = getTwoIndexes(itemBuf,dis);
							CONSTANT_Methodref_Info methodRefValue=new CONSTANT_Methodref_Info(methodIndexes[0], methodIndexes[1]);
							pool.add(methodRefValue);
							printConstantPoolVal(i, "method_indexes", methodIndexes[0] + " " + methodIndexes[1]);
							break;
						case 11:
							int[] interfaceMethodIndexes=getTwoIndexes(itemBuf,dis);
							CONSTANT_InterfaceMethodref_Info interMethodValue = new CONSTANT_InterfaceMethodref_Info(interfaceMethodIndexes[0], interfaceMethodIndexes[1]);
							pool.add(interMethodValue);
							printConstantPoolVal(i, "interfaceMethod_indexes", interfaceMethodIndexes[0] + " " + interfaceMethodIndexes[1]);
							break;
						case 12:
							int[] nameTypeInfo = getTwoIndexes(itemBuf, dis);
							CONSTANT_NameAndType_Info nameTypeValue = new CONSTANT_NameAndType_Info(nameTypeInfo[0], nameTypeInfo[1]);
							pool.add(nameTypeValue);
							printConstantPoolVal(i, "nameType_indexes", nameTypeInfo[0] + " " + nameTypeInfo[1]);
							break;
						default:
							
					}
				}
		
				//resolve access flags
				dis.read(itemBuf, 0, 2);
				resolveFlags(itemBuf);
				
				//resolve this_class
				dis.read(itemBuf,0,2);
				int thisClassIndex = bytesToInt(itemBuf, 2);
				System.out.println("This class index is:    " + thisClassIndex);
				
				//resolve super_class
				dis.read(itemBuf, 0, 2);
				int superClassIndex = bytesToInt(itemBuf,2);
				System.out.println("The super class index is:    " + superClassIndex);
				
				//resolve interfaces
				dis.read(itemBuf, 0, 2);
				int interfaceCount = bytesToInt(itemBuf, 2);
				System.out.println("Interface count:    " + interfaceCount);
				for (int i = 0; i < interfaceCount; i++) {
					dis.read(itemBuf, 0, 2);
					int interfaceIndex=bytesToInt(itemBuf, 2);
					System.out.println("Interface #" + i + " index:    " + interfaceIndex);
				}
				
				//resolve fields
				dis.read(itemBuf, 0, 2);
				int fieldsCount = bytesToInt(itemBuf, 2);
				for (int i = 0; i < fieldsCount; i++) {
					resolveFieldInfo(itemBuf, dis, i);
				}
		
				//TODO to be continued
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
  
	private static int[] getTwoIndexes(byte[] itemBuf, DataInputStream dis) throws IOException {

		dis.read(itemBuf, 0, 2);
		int classrefIndex = bytesToInt(itemBuf, 2);
		dis.read(itemBuf, 0, 2);
		int nameTyperefIndex = bytesToInt(itemBuf, 2);
		int[] result = new int[2];
		result[0] = classrefIndex;
		result[1] = nameTyperefIndex;
		return result;
	}

	private static void resolveFieldInfo(byte[] itemBuf, DataInputStream dis, int index) throws IOException {
		
		System.out.println("Field #" + index);
		dis.read(itemBuf, 0, 2);
		resolveFieldFlags(itemBuf);
		int nameIndex = getInt(itemBuf, dis);
		System.out.println("\tName index:" + nameIndex);
		int descIndex = getInt(itemBuf, dis);
		System.out.println("\tDesc index:" + descIndex);
	}
  
	private static int getInt(byte[] itemBuf, DataInputStream dis) throws IOException {
		dis.read(itemBuf, 0, 2);
		return bytesToInt(itemBuf, 2);
	}
  
	private static void resolveFlags(byte[] bytes) {
		
		byte first = bytes[0];
		ArrayList<AccesFlags> flags = new ArrayList<AccesFlags>();
		if ((first & (1 << 1)) != 0) {
			flags.add(AccesFlags.ACC_INTERFACE);
		}
		if ((first & (1 << 2)) != 0) {
			flags.add(AccesFlags.ACC_ABSTRACT);
		}
		if ((first & (1 << 4)) != 0) {
			flags.add(AccesFlags.ACC_SYNTHETIC);
		}
		if ((first & (1 << 5)) != 0) {
			flags.add(AccesFlags.ACC_ANNOTATION);
		}
		if ((first & (1 << 6)) != 0) {
			flags.add(AccesFlags.ACC_ENUM);
		}

		byte second=bytes[1];
		if ((second & 1) != 0 ) {
			flags.add(AccesFlags.ACC_PUBLIC);
		}
		if ((second & (1 << 4)) != 0) {
			flags.add(AccesFlags.ACC_FINAL);
		}
		if ((second & (1 << 5)) != 0) {
			flags.add(AccesFlags.ACC_SUPER);
		}

		StringBuilder sb = new StringBuilder();
		for (AccesFlags flag : flags) {
			sb.append(flag + "    ");
		}
		System.out.println("Flags: " + sb.toString());
	}
  
	private static void resolveFieldFlags(byte[] bytes) {
	  
	}

	private static String resolveCPUtf8(byte[] itemBuf, DataInputStream dis, ArrayList<ConstantPoolType> pool) throws IOException {

		dis.read(itemBuf, 0, 2);
		int length = bytesToInt(itemBuf, 2);
		StringBuilder sb = new StringBuilder();
/*		boolean newStart = true;
		int bytesLeft = 0;
		ArrayList<Byte> part = new ArrayList<Byte>();
		for (int j = 0; j < length; j++) {
			if (newStart) {
				dis.read(itemBuf, 0, 1);
				if ((itemBuf[0] & ((byte)1 << 7)) == (byte)0) {
					//The highest bit is 0
					byte[] res = new byte[1];
					res[0] = itemBuf[0];
					String temp = new String(res, "UTF-8");
					sb.append(temp);
				} else {
					//The first of the bytes
					newStart = false;
					//TODO may include more severe check. Below code not tested
					if ((itemBuf[0] & ((byte)1 << 5)) == (byte)0) {
						bytesLeft = 2;
						part.add((byte)(itemBuf[0] & ((byte)15)));
					} else {
						bytesLeft = 1;
						part.add((byte)(itemBuf[0] & ((byte)31)));
					}
				}
			} else {
				//The subsequent bytes. Code not tested
				dis.read(itemBuf, 0, 1);
				part.add((byte)(itemBuf[0] & ((byte)63)));
				bytesLeft--;
				
				if (bytesLeft == 0) {
					String tmp = null;
					if (part.size() == 2) {
						int current = ((part.get(0) & 0xFF) << 6) | (part.get(1) & 0xFF);
						tmp = new String(ByteBuffer.allocate(4).putInt(current).array(), "UTF-8");
					} else {
						int current = ((part.get(0) & 0xFF) << 12) | ((part.get(1) & 0xFF) << 6) | (part.get(2) & 0xFF);
						tmp = new String(ByteBuffer.allocate(4).putInt(current).array(), "UTF-8");
					}
					sb.append(tmp);
					newStart = true;
					part = new ArrayList<Byte>();
				}
			}
		}*/
		byte[] buff = new byte[length];
		dis.read(buff, 0, length);
		sb.append(new String(buff, "UTF-8"));
		
		CONSTANT_Utf8_Info string = new CONSTANT_Utf8_Info();
		string.setValue(sb.toString());
		pool.add(string);
		return sb.toString();
	}

	private static final String bytesToHexString(byte[] bArray, int length) {
		
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length && i<length; i++) {
			sTemp=Integer.toHexString((int) (0xFF & bArray[i]));
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	private static void printConstantPoolVal(int i, String type, String value) {
		System.out.println("ConstantPool: #" + (i + 1) + "    " + type + "    " + value);
	}

	private static final int bytesToInt(byte[] bArray, int length) {
		
		int result = bArray[length-1] & 0xFF;
		for (int i = length - 2; i >= 0; i--) {
			result = result | (bArray[i] & 0xFF) << 8 * (length - 1 - i);
		}
		return result;
	}

	private static final long bytesToLong(byte[] bArray) {
		
		int length = 8;
		long result = bArray[length - 1] & 0xFF;
		for(int i = length - 2; i >= 0; i--){
			result = result | (bArray[i] & 0xFF) << 8 * (length - 1 - i);
		}
		return result;
	}

	private static final double bytesToDouble(byte[] bArray) {
		return ByteBuffer.wrap(bArray).getDouble();
	}
	
	public enum AccesFlags {
		ACC_PUBLIC, ACC_FINAL, ACC_SUPER, ACC_INTERFACE, ACC_ABSTRACT, 
		ACC_SYNTHETIC, ACC_ANNOTATION, ACC_ENUM, ACC_PRIVATE, ACC_PROTECTED, 
		ACC_STATIC, ACC_VOLATILE, ACC_TRANSPARENT
	}
}
