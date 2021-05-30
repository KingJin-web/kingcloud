package com.king.kingcloud.biz;

import com.king.kingcloud.bean.User;

/**
 * @program: kingcloud
 * @description:
 * @author: King
 * @create: 2021-05-28 19:04
 */
public interface UserBiz {
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
