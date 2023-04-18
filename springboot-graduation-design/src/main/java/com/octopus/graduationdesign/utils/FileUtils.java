package com.octopus.graduationdesign.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    private static final ConcurrentHashMap<String, Object> uploadLocks = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Object> downloadLocks = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Object> getUploadLocks() {
        return uploadLocks;
    }

    public static void saveFile(String username, MultipartFile file, String targetDirPath) throws IOException {
        // 在目录中创建文件
        BufferedInputStream inputStream = new BufferedInputStream(file.getInputStream());
        String[] split = file.getOriginalFilename().split("\\.");
        String type = split[split.length - 1];
        BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(targetDirPath + "/" + UUID.randomUUID() + "." + type));
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, len);
        }
        inputStream.close();
        fileOutputStream.close();
    }

    public static void zipDirToOutput(String dirPath, OutputStream output) throws IOException {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        //输出流
        ZipOutputStream zipOutputStream = new ZipOutputStream(output);
        for (File file : files) {
            String name = file.getName();
            //读取文件
            FileInputStream inputStream = new FileInputStream(file);
            //ZipEnter:表示压缩文件的条目(文件目录)
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            int len = 0;
            while ((len = inputStream.read()) != -1) {
                zipOutputStream.write(len);
            }
            inputStream.close();
        }
        zipOutputStream.close();
    }
}
