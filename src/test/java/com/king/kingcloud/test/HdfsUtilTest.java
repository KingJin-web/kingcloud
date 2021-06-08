package com.king.kingcloud.test;

import com.king.kingcloud.bean.HdfsFileStatus;
import com.king.kingcloud.util.HdfsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HdfsUtilTest {

    @Autowired
    private HdfsUtil hdfsUtil;

    @Test
    public void setHDFS_PATH() {
    }

    @Test
    public void mkdir() {
    }

    @Test
    public void query() {
        System.out.println(hdfsUtil.query("king", "biz"));
    }

    @Test
    public void queryAll() {
        System.out.println();
        List<HdfsFileStatus> list = hdfsUtil.queryAll("king");
        for (HdfsFileStatus hfs : list) {
            System.out.println(hfs.getPath());
        }
    }

    @Test
    public void queryAllType() {
        List<HdfsFileStatus> list = hdfsUtil.queryAllType("king", 1);
        for (HdfsFileStatus hfs : list) {
            System.out.println(hfs.getPath());
        }
    }
    @Test
    public void Look(){
        hdfsUtil.lookDoc("/king/文件夹1/文件夹4/css/login.css");
    }
    @Test
    public void down() {
        //hdfs://dn1:9000/king/文件夹5/元素定位.jpg
        //hdfs://dn1:9000/king/文件夹1/文件夹4/css/login.css
        byte[] downFile = hdfsUtil.downFile("/king/文件夹1/文件夹4/css/login.css");
        String line = Arrays.toString(downFile);
        System.out.println(line);
    }

    @Test
    public void querya() {
        System.out.println(" ");
        int a = 0;
    }
}