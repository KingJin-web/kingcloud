package com.king.kingcloud.util;

import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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

}
