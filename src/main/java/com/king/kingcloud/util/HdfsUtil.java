package com.king.kingcloud.util;

import com.king.kingcloud.bean.HdfsFileStatus;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 新建文件夹
     * 根目录下新建文件夹  用户目录
     *
     * @param fileName
     */
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


    /**
     * 新建文件夹
     * 新建用户目录下的文件夹
     *
     * @param name
     * @param pathS
     * @return
     */
    public boolean mkdir(String name, String pathS) {
        Path path;
        if (pathS == null || pathS.equals("") || pathS.endsWith("undefined")) {
            path = new Path("/" + name);
        } else {
            path = new Path("/" + name + "/" + pathS);
        }
        boolean result = false;
        try {
            result = fileSystem.mkdirs(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean changeName() {


        return true;
    }

    public boolean changeFileName(String uname, String path, String oldName, String newName) {

        Path oldPath = new Path("/" + uname + "/" + path + "/" + oldName);
        Path newPath = new Path("/" + uname + "/" + path + "/" + newName);
        System.out.println(oldPath);
        System.out.println(newPath);
        // 第一个参数是原文件的名称，第二个则是新的名称
        try {
            fileSystem.rename(oldPath, newPath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 上传文件
     *
     * @param windowPath
     * @param fatherpath
     * @param sonpath
     */
    public Boolean upload(String windowPath, String fatherpath, String sonpath) {
        boolean flag = true;
        try {
            fileSystem.copyFromLocalFile(new Path(windowPath), new Path("/" + fatherpath + "/" + sonpath));
            System.out.println("上传成功");
            return flag;
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
            System.out.println("上传失败 原因：" + e.getMessage());
            return flag;
        }
    }

    public List<HdfsFileStatus> query(String name, String pathS) {
        Path path;
        if (pathS == null || pathS.equals("") || pathS.endsWith("undefined")) {
            path = new Path("/" + name);
        } else {
            path = new Path("/" + name + "/" + pathS);
        }

        FileStatus[] fileStatuses;
        List<HdfsFileStatus> list = new ArrayList<>();
        try {
            fileStatuses = fileSystem.listStatus(path);
            for (FileStatus fileStatus : fileStatuses) {
                HdfsFileStatus hfs = new HdfsFileStatus();
                //获取文件名
                hfs.setName(fileStatus.getPath().getName());
                hfs.setPath(getPathDeName(name, fileStatus.getPath()));
                //获取文件是否为文件夹
                hfs.setIsDirectory(fileStatus.isDirectory());
                //文件上次修改时间
                hfs.setModification_time(TimeUtil.timeToString(fileStatus.getModificationTime()));
                hfs.setAccess_time(TimeUtil.timeToString(fileStatus.getAccessTime()));
                //获取文件实际大小
                hfs.setFileSize((fileSystem.getContentSummary(fileStatus.getPath()).getSpaceConsumed()) / 3);
                hfs.setBlocksize(fileStatus.getBlockSize());
                list.add(hfs);
            }
        } catch (IOException e) {
            e.printStackTrace();
            list.add(new HdfsFileStatus(false));
        }
        return list;
    }


    public boolean delete(String name, String dlPath) {
        Path path;
        if (dlPath == null || dlPath.equals("") || dlPath.endsWith("undefined")) {
            return false;
        } else {
            path = new Path("/" + name + "/" + dlPath);
        }
        // 第二个参数指定是否要递归删除，false=否，true=是
        try {
            fileSystem.delete(path, false);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getFileName(Path path) {
        Assert.notNull(path, "");
        String file = String.valueOf(path);
        return file.substring(file.lastIndexOf("/") + 1);
    }

    /**
     * 返回文件的路径
     * String a = "hdfs://dn1:9000/king/文件夹/a";
     *
     * @param name king
     * @param path hdfs://dn1:9000/king/文件夹/a
     * @return /文件夹/a
     */
    public String getPathDeName(String name, Path path) {
        String paths = String.valueOf(path);
        return paths.substring(paths.indexOf(name) + name.length());
    }
}
