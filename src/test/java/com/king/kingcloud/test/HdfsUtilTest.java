package com.king.kingcloud.test;

import com.king.kingcloud.util.HdfsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        hdfsUtil.query("king","");
    }
}