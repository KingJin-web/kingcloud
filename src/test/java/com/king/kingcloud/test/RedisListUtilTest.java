package com.king.kingcloud.test;


import com.king.kingcloud.bean.User;
import com.king.kingcloud.biz.UserBizImpl;
import com.king.kingcloud.util.RedisUtil;
import com.king.kingcloud.vo.UserVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisListUtilTest {

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void push() {

    }

    @Test
    public void insert() {
        String session = "1234";
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", "king");
        hashMap.put("id", String.valueOf(123));
        redisUtil.insert(session, hashMap);
    }
    @Autowired
    private UserBizImpl userBiz;

    @Test
    public void testPush() {
        User user = userBiz.getUserByName("king");
        UserVo userVo = new UserVo();
        userVo.setName(user.getName());
        userVo.setEmail(user.getEmail());
        userVo.setValidateCode("Ashd1");
        List<UserVo> list = new ArrayList<>();
        list.add(userVo);


    }
    @Test
    public void GetUserVo(){
        redisUtil.getUserVo("6F8156011FB4E64ACCA6AE1B837C4EC4");
    }
    @Test
    public void lSet() {
    }
}