package com.king.kingcloud.vo;

import lombok.Data;

/**
 * @program: kingcloud
 * @description: layui 规定的json 格式
 * @author: King
 * @create: 2021-06-06 11:48
 */
@Data
public class JsonLayui {
    private Integer code;
    private String msg;
    //数据的多少
    private Integer count;
    private Object data;
//    "code":0,"msg":"","count":1000,"data"
}
