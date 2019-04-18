/**
 * @Title:  FileUtils.java
 * @Package: com.asiainfo.apacheutils
 * @Description: TODO
 * @author: chenzq
 * @date:   2019年2月8日 上午10:31:20
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
package com.asiainfo.apacheutils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.asiainfo.encrypt.MD5Util;

/**   
 * @Description: 文件操作工具类
 * @author chenzq  
 * @date 2019年2月8日 上午10:31:20
 * @version V1.0  
 */
public class FileUtils {

    public static final int EOF = -1;
    @SuppressWarnings("restriction")
    public static final String LINE_SEPARATOR = 
            java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));;
    
    private FileUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }
    
    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(String filePath) {
        return StringUtils.isEmpty(filePath) ? null : new File(filePath);
    }
  
    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    /**
     * 判断是否是目录
     *
     * @param dirPath 目录路径
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isDir(String dirPath) {
        return isDir(getFileByPath(dirPath));
    }

    /**
     * 判断是否是目录
     *
     * @param file 文件
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isDir(File file) {
        return isFileExists(file) && file.isDirectory();
    }
    
    /**
     * 判断是否是文件
     *
     * @param filePath 文件路径
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isFile(String filePath) {
        return isFile(getFileByPath(filePath));
    }

    /**
     * 判断是否是文件
     *
     * @param file 文件
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isFile(File file) {
        return isFileExists(file) && file.isFile();
    }
    
    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param dirPath 文件路径
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 获取目录下所有文件，包括目录本身，由isRecursive参数决定是否递归进子目录
     *
     * @param dirPath     目录路径
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    public static List<File> listFilesInDir(String dirPath, boolean isRecursive) {
        return listFilesInDir(getFileByPath(dirPath), isRecursive);
    }

    /**
     * 获取目录下所有文件，包括目录本身，由isRecursive参数决定是否递归进子目录
     *
     * @param dir         目录
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    public static List<File> listFilesInDir(File dir, boolean isRecursive) {
        if (dir == null || !isDir(dir)) return null;
        if (isRecursive) return listFilesInDir(dir);
        List<File> list = new ArrayList<>();
        Collections.addAll(list, dir.listFiles());
        return list;
    }

    /**
     * 获取目录下所有文件，包括目录本身及目录下的所有文件
     *
     * @param dirPath 目录路径
     * @return 文件链表
     */
    public static List<File> listFilesInDir(String dirPath) {
        return listFilesInDir(getFileByPath(dirPath));
    }

    /**
     * 获取目录下所有文件，包括目录本身及目录下的所有文件
     *
     * @param dir 目录
     * @return 文件链表
     */
    public static List<File> listFilesInDir(File dir) {
        if (dir == null || !isDir(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                list.add(file);
                if (file.isDirectory()) {
                    list.addAll(listFilesInDir(file));
                }
            }
        }
        return list;
    }

    /**
     * 获取目录下所有后缀名为suffix的文件
     * <p>大小写忽略</p>
     *
     * @param dirPath     目录路径
     * @param suffix      后缀名
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    public static List<File> listFilesInDirWithFilter(String dirPath, String suffix, boolean isRecursive) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), suffix, isRecursive);
    }

    /**
     * 获取目录下所有后缀名为suffix的文件
     * <p>大小写忽略</p>
     *
     * @param dir         目录
     * @param suffix      后缀名
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    public static List<File> listFilesInDirWithFilter(File dir, String suffix, boolean isRecursive) {
        if (dir == null || !isDir(dir)) return null;
        if (isRecursive) return listFilesInDirWithFilter(dir, suffix);
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    /**
     * 获取目录下所有后缀名为suffix的文件包括子目录
     * <p>大小写忽略</p>
     *
     * @param dirPath 目录路径
     * @param suffix  后缀名
     * @return 文件链表
     */
    public static List<File> listFilesInDirWithFilter(String dirPath, String suffix) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), suffix);
    }

    /**
     * 获取目录下所有后缀名为suffix的文件包括子目录
     * <p>大小写忽略</p>
     *
     * @param dir    目录
     * @param suffix 后缀名
     * @return 文件链表
     */
    public static List<File> listFilesInDirWithFilter(File dir, String suffix) {
        if (dir == null || !isDir(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file);
                }
                if (file.isDirectory()) {
                    list.addAll(listFilesInDirWithFilter(file, suffix));
                }
            }
        }
        return list;
    }
    
    /**
     * 获取文件编码格式
     * @param file
     * @return
     */
    public static String getFileCharset(File file) {
        int p = 0;
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            p = (is.read() << 8) + is.read();
        } catch (IOException e) {
            //ignore
        } finally {
            closeQuietly(is);
        }
        switch (p) {
            case 0xefbb:
                return "UTF-8";
            case 0xfffe:
                return "Unicode";
            case 0xfeff:
                return "UTF-16BE";
            default:
                return "GBK";
        }
    }
    
    /**
     * 读取文件所有行，使用系统默认编码
     * @param file
     * @return
     */
    public static List<String> readLines(File file) {
        return readLines(file, (String) null);
    }
    
    /**
     * 读取文件所有行，指定编码
     * @param file
     * @param encoding
     * @return
     */
    public static List<String> readLines(File file, String encoding) {
        if (file == null || !isFile(file)) return null;
        List<String> list = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = StringUtils.isEmpty(encoding) ? 
                    new BufferedReader(new InputStreamReader(new FileInputStream(file))) : 
                        new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            //ignore
        } finally {
            closeQuietly(reader);
        }
        return list;
    }
    
    /**
     * 写入多行到文件中，缺省编码
     * @param file
     * @param lines
     * @param lineSeparator
     */
    public static void writeLines(File file, Collection<?> lines, String lineSeparator) {
        writeLines(file, lines, lineSeparator, (String) null);
    }
    
    /**
     * 写入多行到文件中，指定编码
     * @param file
     * @param lines
     * @param lineSeparator
     * @param encoding
     */
    public static void writeLines(File file, Collection<?> lines, String lineSeparator, String encoding) {
        if (file == null || !isFile(file)) return;
        if (lines == null || lines.isEmpty()) return;
        String lineEnding = StringUtils.isEmpty(lineSeparator) ? LINE_SEPARATOR : lineSeparator;
        Writer writer = null;
        try {
            writer = StringUtils.isEmpty(encoding) ? 
                    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file))) : 
                        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
            for (final Object line : lines) {
                if (line != null) {
                    writer.write(line.toString());
                }
                writer.write(lineEnding);
            }
        } catch (IOException e) {
            //ignore
        } finally {
            closeQuietly(writer);
        }
    }
    
    /**
     * 获取文件行数
     * @param file 文件
     * @return 文件行数
     */
    public static int countLines(File file) {
        return countLines(file, (String) null);
    }
    
    /**
     * 获取文件行数，指定编码
     * @param file 文件
     * @param encoding 编码
     * @return 文件行数
     */
    public static int countLines(File file, String encoding) {
        if (file == null || !isFile(file)) return 0;
        int count = 0;
        BufferedReader reader = null;
        try {
            InputStreamReader isReader = StringUtils.isEmpty(encoding) ? 
                    new InputStreamReader(new FileInputStream(file)) : 
                        new InputStreamReader(new FileInputStream(file), encoding);
            reader = new BufferedReader(isReader);
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            //ignore
        } finally {
            closeQuietly(reader);
        }
        return count;
    }
    
    /**
     * 获取文件的MD5校验码
     *
     * @param filePath 文件
     * @return 文件的MD5校验码
     */
    public static String getFileMD5(String filePath) {
        return getFileMD5(getFileByPath(filePath));
    }
    
    /**
     * 获取文件的MD5校验码
     *
     * @param file 文件
     * @return 文件的MD5校验码
     */
    public static String getFileMD5(File file) {
        if (file == null || !isFile(file)) return null;
        String result = null;
        try {
            result = MD5Util.encryptMD5Hex(toByteArray(file));
        } catch (NoSuchAlgorithmException e) {
            //ignore
        }
        return result;
    }
    
    /**
     * 读取文件所有字节到字节数组中
     * @param file
     * @return
     */
    public static byte[] toByteArray(File file) {
        if (file == null || !isFile(file)) return null;
        byte[] result = null;
        InputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            in = new FileInputStream(file);
            byte[] buff = new byte[1024];
            int n;
            while (EOF != (n = in.read(buff))) {
                out.write(buff, 0, n);
            }
            result = out.toByteArray();
            in.close();
        } catch (IOException e) {
            //ignore
        } finally {
            closeQuietly(in, out);
        }
        return result;
    }
    
    /**
     * 获取全路径中的不带拓展名的文件名
     *
     * @param file 文件
     * @return 不带拓展名的文件名
     */
    public static String getFileNameNoExtension(File file) {
        if (file == null || !isFile(file)) return null;
        String fileName = file.getName();
        return fileName.lastIndexOf(".") < 0 ? fileName : fileName.substring(0, fileName.lastIndexOf("."));
    }
    
    /**
     * 获取全路径中的文件拓展名
     *
     * @param file 文件
     * @return 文件拓展名
     */
    public static String getFileExtension(File file) {
        if (file == null || !isFile(file)) return null;
        String fileName = file.getName();
        return fileName.lastIndexOf(".") < 0 ? null : fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    
    /**
     * 关闭IO
     *
     * @param closeables closeable
     */
    public static void closeQuietly(final Closeable... closeables) {
        if (closeables == null) return;
        try {
            for (final Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
    
    /**
     * @Description: TODO
     * @author chenzq
     * @date 2019年2月8日 上午10:31:20
     * @param args
     */
    public static void main(String[] args) {

        System.out.println(getFileMD5("src/main/java/com/asiainfo/apacheutils/foo.txt"));
        System.out.println(countLines(getFileByPath("src/main/java/com/asiainfo/apacheutils/foo.txt"), ""));
        System.out.println(getFileExtension(getFileByPath("src/main/java/com/asiainfo/apacheutils/foo.txt")));
        System.out.println(getFileNameNoExtension(getFileByPath("src/main/java/com/asiainfo/apacheutils/foo.txt")));
    }
}
