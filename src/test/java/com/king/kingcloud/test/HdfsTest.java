package com.king.kingcloud.test;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @program: hdfs
 * @description: hdfs测试
 * @author: King
 * @create: 2021-05-27 19:16
 */

public class HdfsTest {
    // HDFS文件系统服务器的地址以及端口
    public static final String HDFS_PATH = "hdfs://dn1:9000";
    // HDFS文件系统的操作对象
    FileSystem fileSystem = null;
    // 配置对象
    Configuration configuration = null;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        // 第一参数是服务器的URI，第二个参数是配置对象，第三个参数是文件系统的用户名
        System.out.println(configuration.toString());
        fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, "root");
        System.out.println("HDFSAPP.setUp");


        //方案二
//        System.setProperty("HADOOP_USER_NAME","root");
//        configuration.set("fs.defaultFS","hdfs://node1:9000");
//        fileSystem =FileSystem.get(configuration);
    }

    /**
     * 创建HDFS目录
     */
    @Test
    public void mkdir() throws Exception {
        // 需要传递一个Path对象
        boolean result = fileSystem.mkdirs(new Path("/king/中文"));
        result = fileSystem.mkdirs(new Path("/king/文件夹4"));
        result = fileSystem.mkdirs(new Path("/king/文件夹6"));
        result = fileSystem.mkdirs(new Path("/king/文件夹5"));
        System.out.println(result);
    }
    //hdfs://node1:9000/king/c91-s2-xm.sql


    /**
     * 创建文件
     */
    public void write(String s) throws Exception {
        // 创建文件
        FSDataOutputStream outputStream = fileSystem.create(new Path("/king/" + s));
        // 写入一些内容到文件中
        outputStream.write(("Hello Hadoop! Hello Java! Hello 世界!" + "Hello Hadoop! Hello Java! Hello 世界!" +"Hello Hadoop! Hello Java! Hello 世界!" +
                "\nHello Hadoop! Hello Java! Hello 世界!").getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    @Test
    public void write()  {
        try {
            write("文件4.txt");     write("文件5.txt");
            write("文件6.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void read() throws IOException {

        FSDataInputStream fsDataInputStream = fileSystem.open(new Path("/king/文件1.txt"));

        InputStreamReader isr = new InputStreamReader(fsDataInputStream);
        BufferedReader bf = new BufferedReader(isr);
        String str;
        while ((str = bf.readLine()) != null) {
            System.out.println(str);
        }
        bf.close();
        isr.close();
        fsDataInputStream.close();
    }

    /**
     * 重命名文件
     *
     * @throws IOException
     */
    @Test
    public void rename() throws IOException {
        Path oldPath = new Path("/king/a.txt");
        Path newPath = new Path("/king/b.txt");
        // 第一个参数是原文件的名称，第二个则是新的名称
        fileSystem.rename(oldPath, newPath);
    }

    /**
     * 删除文件
     *
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
        // 第二个参数指定是否要递归删除，false=否，true=是
        fileSystem.delete(new Path("/king/b.txt"), false);
    }

    /**
     * 上传本地文件到HDFS
     *
     * @throws Exception
     */
    @Test
    public void upload() throws Exception {
        Path localPath = new Path("E:\\Users\\King\\Documents\\s2\\C91-S2-XM\\src\\dao\\IndexXuanRanDao.java");
        Path hdfsPath = new Path("/king/test/");
        // 第一个参数是本地文件的路径，第二个则是HDFS的路径
        fileSystem.copyFromLocalFile(localPath, hdfsPath);
    }

    /**
     * HDFS 中下载文件
     *
     * @throws IOException
     */
    @Test
    public void download() throws IOException {
        Path path1 = new Path("/king/中文.txt");
        Path path2 = new Path("D:/");
        fileSystem.copyToLocalFile(path1, path2);
    }

    @Test
    public void query() throws IOException {
        Path path = new Path("/");
        FileStatus[] fileStatuses = fileSystem.listStatus(path);
        for (int i = 0; i < fileStatuses.length; i++) {
            System.out.println(fileStatuses[i]);
            System.out.println(fileStatuses[i].getPath().toString());
        }
    }

    /**
     * 查看某个文件信息
     * hdfs://node1:9000/king/test/IndexXuanRanDao.java
     *
     * @throws Exception
     */
    @Test
    public void checkFile() throws IOException {
        FileStatus status = fileSystem.getFileStatus(new Path("/king"));
        BlockLocation[] blockLocations = fileSystem.getFileBlockLocations(status, 0, status.getLen());
        for (BlockLocation bl : blockLocations) {
            System.out.println(Arrays.toString(bl.getHosts()) + "\t" + Arrays.toString(bl.getNames()));
            System.out.println(Arrays.toString(blockLocations));
        }
    }

    /**
     * 获取HDFS集群中所有数据节点信息
     *
     * @throws IOException
     */
    @Test
    public void checkAll() throws IOException {
        DistributedFileSystem fs = (DistributedFileSystem) fileSystem;
        DatanodeInfo[] datanodeInfos = fs.getDataNodeStats();
        for (DatanodeInfo df : datanodeInfos) {
            System.out.println(df.getHostName() + "\t" + df.getName());
        }
    }

    // 释放资源
    @After
    public void tearDown() throws Exception {
        configuration = null;
        fileSystem = null;

        System.out.println("HDFSAPP.tearDown");
    }
}
