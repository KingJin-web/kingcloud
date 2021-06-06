package com.king.kingcloud.vo;

import com.king.kingcloud.bean.User;
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

    private Integer uid;
    private String name;
    private String email;
    private String validateCode;

}
