package com.king.kingcloud.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @program: kingcloud
 * @description:
 * @author: King
 * @create: 2021-06-04 20:38
 */
public class EmptyUtil extends Assert {
    public EmptyUtil() {
        super();
    }

    public static String notNull(String msg, Object... objects) {
        try {
            for (Object obj : objects) {
                EmptyUtil.notNull(obj, msg);
            }
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return null;
    }

    /**
     * 判断多个字符串是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String... s) {
        for (String str : s) {
            return isEmpty(str);
        }
        return false;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return "".equals(str);
    }

    /**
     * 提供字符串，集合，数组，map等常见对象判空处理
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            return "".equals(str.trim());
        }
        if (obj instanceof Number) {
            Number num = (Number) obj;
            return num.byteValue() == 0;
        }
        if (obj instanceof Collection) {
            Collection col = (Collection) obj;
            return col.isEmpty();
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return map.isEmpty();
        }
        if (obj.getClass().getSimpleName().endsWith("[]")) {
            List<Object> list = Arrays.asList(obj);
            Object[] objs = (Object[]) list.get(0);
            return objs.length == 0;
        }
        return false;
    }

    /**
     * 提供字符串，集合，数组，map等常见对象判空处理
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

}
