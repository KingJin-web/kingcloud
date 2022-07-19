package com.king.kingcloud.util;

import org.springframework.util.ObjectUtils;


/**
 * @author: King
 * @project: kingcloud
 * @date: 2022年07月18日 19:32
 * @description:
 */
public class StringUtils extends ObjectUtils {
    //正则表达式：验证用户名，只能包含英文、数字、下划线，长度为4-20
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_]{4,20}$";
    //正则表达式：验证密码，只能包含英文、数字、下划线，长度为4-20
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9_]{4,20}$";
    //正则表达式：验证手机号，符合格式："^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$"
    public static final String REGEX_MOBILE = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
    //正则表达式：验证邮箱，符合格式："^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 是否为邮箱
     *
     * @param s
     * @return
     */
    public static boolean isEmail(String s) {
        return isMatch(REGEX_EMAIL, s);
    }

    /**
     * 是否为手机号
     *
     * @param s
     * @return
     */
    public static boolean isMobile(String s) {
        return isMatch(REGEX_MOBILE, s);
    }

    /**
     * 用户名校验
     *
     * @param s
     * @return
     */
    public static boolean isUsername(String s) {
        return isMatch(REGEX_USERNAME, s);
    }

    /**
     * 密码校验
     *
     * @param s
     * @return
     */
    public static boolean isPassword(String s) {
        return isMatch(REGEX_PASSWORD, s);
    }

    private static boolean isMatch(String regex, String s) {
        if (isEmpty(s)) {
            throw new MyException("必填项不能为空!");
        }
        return s.matches(regex);
    }

    public static boolean isEmpty(String ...s) {
        for (String str : s) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }
}