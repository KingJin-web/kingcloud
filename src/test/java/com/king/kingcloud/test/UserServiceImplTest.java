package com.king.kingcloud.test;

import com.king.kingcloud.entity.User;
import com.king.kingcloud.service.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userBiz;

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