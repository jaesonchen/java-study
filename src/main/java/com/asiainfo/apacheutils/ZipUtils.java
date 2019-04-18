/**
 * @Title:  ZipUtils.java
 * @Package: com.asiainfo.apacheutils
 * @Description: TODO
 * @author: chenzq
 * @date:   2019年2月8日 下午7:08:53
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
package com.asiainfo.apacheutils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**   
 * @Description: 压缩文件处理工具
 * @author chenzq  
 * @date 2019年2月8日 下午7:08:53
 * @version V1.0  
 */
public class ZipUtils {

    static final Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);
    
    private ZipUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 压缩文件、文件夹到压缩流
     * @param file
     * @param zos
     * @param baseDir
     * @return
     */
    public static boolean compress(File file, ZipOutputStream zos, String baseDir) {
        return compress(file, zos, baseDir, (String) null);
    }
    
    /**
     * 压缩文件、文件夹到压缩流
     * @param file
     * @param zos
     * @param baseDir
     * @param comment
     * @return
     */
    public static boolean compress(File file, ZipOutputStream zos, String baseDir, String comment) {
        if (file == null || !file.exists()) return false;
        if (file.isFile()) {
            return compressFile(file, zos, baseDir, comment);
        }
        for (File f : file.listFiles()) {
            if (!compress(f, zos, baseDir + File.separator + file.getName(), comment)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 压缩单个文件到压缩流
     * @param file
     * @param zos
     * @param baseDir
     * @param comment
     * @return
     */
    public static boolean compressFile(File file, ZipOutputStream zos, String baseDir, String comment) {
        if (file == null || !FileUtils.isFile(file)) return false;
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            String basePath = StringUtils.isEmpty(baseDir) ? file.getName() : (baseDir + File.separator + file.getName());
            ZipEntry entry = new ZipEntry(basePath);
            if (!StringUtils.isEmpty(comment)) {
                entry.setComment(comment);
            }
            zos.putNextEntry(entry);
            byte[] buf = new byte[1024];
            int count;
            while ((count = bis.read(buf)) != -1) {
                zos.write(buf, 0, count);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("error on compressFile!", e);
        } finally {
            FileUtils.closeQuietly(bis);
        }
        return false;
    }
    
    /**
     * 批量压缩文件、文件夹
     * @param files
     * @param zipFile
     * @return
     */
    public static boolean zipFiles(Collection<File> files, File zipFile) {
        return zipFiles(files, zipFile, "", (String) null);
    }
    
    /**
     * 批量压缩文件、文件夹
     * @param files
     * @param zipFile
     * @param baseDir
     * @return
     */
    public static boolean zipFiles(Collection<File> files, File zipFile, String baseDir) {
        return zipFiles(files, zipFile, baseDir, (String) null);
    }
    
    /**
     * 批量压缩文件、文件夹
     * @param files 待压缩文件集合
     * @param zipFile  压缩文件
     * @param baseDir  压缩基准目录
     * @param comment  压缩文件的注释
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     */
    public static boolean zipFiles(Collection<File> files, File zipFile, String baseDir, String comment) {
        if (files == null || files.isEmpty()) return false;
        if (zipFile == null) return false;
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            for (File f : files) {
                if (!compress(f, zos, baseDir, comment)) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("error on zipFiles!", e);
        } finally {
            FileUtils.closeQuietly(zos);
        }
        return false;
    }
    
    /**
     * 压缩单个文件、文件夹
     * @param file
     * @param zipFile
     * @param baseDir
     * @return
     */
    public static boolean zip(File file, File zipFile) {
        if (file == null || !FileUtils.isFile(file)) return false;
        if (zipFile == null) return false;
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            if (!compress(file, zos, "", (String) null)) {
                return false;
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("error on zip!", e);
        } finally {
            FileUtils.closeQuietly(zos);
        }
        return false;
    }
    
    /**
     * 解压文件
     * @param file
     * @param destDir
     * @return
     */
    public static boolean unzip(File file, String destDir) {
        if (file == null || !FileUtils.isFile(file)) return false;
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(file);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    String dirPath = destDir + File.separator + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(destDir + File.separator + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();
                    // 将压缩文件内容写入到这个文件中
                    InputStream is = zipFile.getInputStream(entry);
                    OutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    // 关流顺序，先打开的后关闭
                    fos.close();
                    is.close();
                }
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("error on unzip!", e);
        } finally {
            FileUtils.closeQuietly(zipFile);
        }
        return false;
    }
    
    /**
     * @Description: TODO
     * @author chenzq
     * @date 2019年2月8日 下午7:08:53
     * @param args
     */
    public static void main(String[] args) {
        
        System.out.println(zipFiles(Arrays.asList(new File[] {new File("src/main/java/com/asiainfo/apacheutils/foo.txt"), 
                new File("src/main/java/com/asiainfo/apacheutils/foo_copy.txt"),
                new File("src/main/java/com/asiainfo/apacheutils/sub/apacheutils.rar")}), 
                new File("src/main/java/com/asiainfo/apacheutils/foo.zip")));
        
        System.out.println(unzip(new File("src/main/java/com/asiainfo/apacheutils/foo.zip"), 
                "src/main/java/com/asiainfo/apacheutils"));
    }
}
