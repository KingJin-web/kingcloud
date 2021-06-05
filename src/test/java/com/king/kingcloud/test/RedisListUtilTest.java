package com.king.kingcloud.test;


import com.king.kingcloud.bean.User;
import com.king.kingcloud.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisListUtilTest {

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void push() {
        redisUtil.push();
    }

    @Test
    public void insert() {
        String session = "1234";
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", "king");
        hashMap.put("id", String.valueOf(123));
        redisUtil.insert(session, hashMap);
    }

    @Test
    public void testPush() {
    }

    @Test
    public void lSet() {
    }
}