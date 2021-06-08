package com.king.kingcloud.util;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.hadoop.conf.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.zip.ZipOutputStream;

/**
 * @program: kingcloud
 * @description:
 * @author: King
 * @create: 2021-06-04 19:13
 */
public class FileUtil {
    /**
     * 在basePath下保存上传的文件夹
     *
     * @param basePath
     * @param files
     */
    public static boolean saveMultiFile(String basePath, MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return false;
        }
        for (MultipartFile file : files) {
            String filePath = basePath + "/" + file.getOriginalFilename();
            makeDir(filePath);
            File dest = new File(filePath);
            try {
                file.transferTo(dest);
                System.out.println(dest.getPath());
            } catch (IllegalStateException | IOException e) {

                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    /**
     * 确保目录存在，不存在则创建
     *
     * @param filePath
     */
    private static void makeDir(String filePath) {
        if (filePath.lastIndexOf('/') > 0) {
            String dirPath = filePath.substring(0, filePath.lastIndexOf('/'));
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    /**
     * 下载文件夹
     *
     * @param path
     * @param filename
     * @param request
     * @return
     * @throws IOException
     */
    public static ResponseEntity downDir(String path, String filename, HttpServletRequest request) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"",
                URLEncoder.encode(filename + ".zip", "utf-8")));

        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Language", "UTF-8");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));

        ByteArrayOutputStream stream = (ByteArrayOutputStream) downloadDir(path);
        byte[] bytes = stream.toByteArray();
        stream.close();

        return new ResponseEntity<>(bytes, headers, HttpStatus.SC_OK);
    }

    public static OutputStream downloadDir(String path) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ZipOutputStream stream1 = new ZipOutputStream(stream);
        new HdfsUtil().compress(path, stream1);
        stream1.close();
        return stream;
    }
}
