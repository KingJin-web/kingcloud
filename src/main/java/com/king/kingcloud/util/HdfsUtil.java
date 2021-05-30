package com.king.kingcloud.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @program: kingcloud
 * @description:
 * @author: King
 * @create: 2021-05-30 17:06
 */
@Repository
public class HdfsUtil {
    // HDFS文件系统服务器的地址以及端口
    private String HDFS_PATH = "hdfs://dn1:9000";
    // HDFS文件系统的操作对象
    private FileSystem fileSystem = null;
    // 配置对象
    private Configuration configuration = null;

    public HdfsUtil() {
        configuration = new Configuration();
        // 第一参数是服务器的URI，第二个参数是配置对象，第三个参数是文件系统的用户名
        try {
            fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, "root");
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public HdfsUtil(String HDFS_PATH) {
        this.HDFS_PATH = HDFS_PATH;
        configuration = new Configuration();
        // 第一参数是服务器的URI，第二个参数是配置对象，第三个参数是文件系统的用户名
        try {
            fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, "root");
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setHDFS_PATH(String HDFS_PATH) {
        this.HDFS_PATH = HDFS_PATH;
    }

    public void mkdir(String fileName) {
        // 需要传递一个Path对象
        boolean result = false;
        try {
            result = fileSystem.mkdirs(new Path("/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }

    public void query(String name, String pathS) {
        Path path = new Path("/" + name + pathS);
        FileStatus[] fileStatuses = new FileStatus[0];
        try {
            fileStatuses = fileSystem.listStatus(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < fileStatuses.length; i++) {
            //System.out.println(fileStatuses[i]);
            System.out.println(fileStatuses[i].getPath());
        }
        for (FileStatus fileStatus: fileStatuses){
            System.out.println(fileStatus);
        }
    }
}
