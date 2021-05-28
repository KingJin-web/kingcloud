package com.king.kingcloud.biz;

import com.king.kingcloud.bean.User;
import com.king.kingcloud.jpaRepository.UserDao;
import jdk.nashorn.internal.parser.DateParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: kingcloud
 * @description: 用户
 * @author: King
 * @create: 2021-05-28 19:07
 */
@Repository
public class UserBizImpl implements UserBiz {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserDao userDao;

    private Example<User> em;

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        String pwd = encoder.encode(user.getPwd());
        if (user.getName() == null || user.getPwd() == null) {
            return false;
        }
        if (isUserName(user)) {
            return false;
        }
        user.setPwd(pwd);
        userDao.save(user);
        return true;
    }

    /**
     * 判断用户名是否呗使用
     *
     * @param user
     * @return
     */
    public boolean isUserName(User user) {
        user.setPwd(null);
        em = Example.of(user);
        return userDao.findAll(em).size() >= 1;
    }

    @Override
    public boolean login(User user) {
        if (user.getName() == null || user.getPwd() == null) {
            return false;
        }
        System.out.println(user);
        String pwd = user.getPwd();
        user.setPwd(null);
        em = Example.of(user);
        User user1 = userDao.findAll(em).get(0);
        System.out.println(user1);
        return encoder.matches(pwd, user1.getPwd());
    }


}
