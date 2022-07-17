package com.king.kingcloud.service;

import com.king.kingcloud.entity.User;

/**
 * @program: kingcloud
 * @description:
 * @author: King
 * @create: 2021-05-28 19:04
 */
public interface UserService {
    /**
     * 注册
     *
     * @param user
     * @return
     */
    public boolean register(User user);


    /**
     * 登录
     *
     * @param user
     * @return
     */
    public boolean login(User user);

    /**
     * 通过用户名查询用户信息
     * @param name
     * @return
     */
    public User getUserByName(String name);
}
