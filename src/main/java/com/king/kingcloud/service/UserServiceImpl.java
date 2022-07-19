package com.king.kingcloud.service;

import com.king.kingcloud.entity.User;
import com.king.kingcloud.jpaRepository.UserDao;
import com.king.kingcloud.util.MyException;
import com.king.kingcloud.util.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserDao userDao;

    private Example<User> em;

    Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        logger.info("注册用户：" + user.getPwd());
        if (StringUtils.isEmpty(user)) {
            throw new MyException("用户信息不能为空!");
        }
        if (!StringUtils.isUsername(user.getName())) {
            throw new MyException("用户名只能包含英文、数字、下划线，长度为4-20！");
        }
        if (!StringUtils.isPassword(user.getPwd())) {
            throw new MyException("密码只能包含英文、数字、下划线，长度为4-20！");
        }
        if (!StringUtils.isEmail(user.getEmail())) {
            throw new MyException("请输入正确邮箱格式!");
        }
        if (isUserName(user)) {
            throw new MyException("用名已存在!");
        }

        logger.info("注册用户：" + user);
        logger.info("注册用户：" + user.getName());
        logger.info("注册用户：" + user.getPassword());

        String pwd = encoder.encode(user.getPwd());
        user.setPwd(pwd);
        userDao.save(user);
        return true;

    }

    /**
     * 判断用户名是否被使用
     *
     * @param user
     * @return
     */
    public boolean isUserName(User user) {
        return userDao.findByName(user.getName()).size() >= 1;
    }

    @Override
    public boolean login(User user) {
        String pwd = user.getPwd();
        user.setPwd(null);
        em = Example.of(user);
        List<User> users = userDao.findAll(em);
        if (users.size() == 0) {
            return false;
        }
        User user1 = users.get(0);

        return encoder.matches(pwd, user1.getPwd());
    }

    /**
     * 通过用户名查询用户信息
     *
     * @param name
     * @return
     */
    @Override
    public User getUserByName(String name) {
        User user = new User();
        user.setName(name);
        em = Example.of(user);
        user = userDao.findAll(em).get(0);
        user.setPwd(null);
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //判断是输入的是用户名还是邮箱
        User user;

        List<User> users;
        if (StringUtils.isEmail(username)) {
            //是邮箱
            users = userDao.findByEmail(username);
        } else {
            //是用户名
            users = userDao.findByName(username);
        }
        if (StringUtils.isEmpty(users)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return users.get(0);
    }
}
