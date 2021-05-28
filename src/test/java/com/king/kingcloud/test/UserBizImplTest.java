package com.king.kingcloud.test;

import com.king.kingcloud.bean.User;
import com.king.kingcloud.biz.UserBizImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserBizImplTest {
    @Autowired
    private UserBizImpl userBiz;

    @Test
    public void register() {
        User user = new User();
        user.setName("蔡徐坤");
        user.setPwd("aaaa");
        Assert.assertFalse(userBiz.register(user));;
    }

    @Test
    public void login() {
        User user = new User();
        user.setName("蔡徐坤");
        user.setPwd("aaaa");
        Assert.assertTrue(userBiz.login(user));
    }

    @Test
    public  void matchesPwd() {
    }
}