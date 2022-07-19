package com.king.kingcloud.vo;

import com.king.kingcloud.entity.User;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: kingcloud
 * @description:
 * @author: King
 * @create: 2021-06-06 08:26
 */
@Data
public class UserVo implements Serializable {

    private Long uid;
    private String name;
    private String email;
    private String validateCode;
    //头像路径
    private String avatar;

    public static UserVo getUserVo(User user) {
        UserVo userVo = new UserVo();
        userVo.setUid(user.getId());
        userVo.setName(user.getName());
        userVo.setEmail(user.getEmail());
        userVo.setAvatar(user.getAvatar());
        return userVo;
    }

}
