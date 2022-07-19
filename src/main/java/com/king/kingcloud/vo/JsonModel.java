package com.king.kingcloud.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * @program: kingcloud
 * @description:
 * @author: King
 * @create: 2021-05-28 21:29
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class JsonModel implements Serializable {


    private static final long serialVersionUID = 5897449533099355749L;
    private Integer code;
    private String msg;
    private Object obj;
    private String sessionId;

    public JsonModel() {
    }

    public JsonModel(Integer code, String msg, Object obj, String sessionId) {
        this.code = code;
        this.msg = msg;
        this.obj = obj;
        this.sessionId = sessionId;
    }

    public JsonModel(Integer code, String msg, Object obj) {
        this.code = code;
        this.msg = msg;
        this.obj = obj;
    }

    public static JsonModel success(Object obj) {
        return new JsonModel(1, "success", obj);
    }

    public static JsonModel success(Object obj, String sessionId) {
        return new JsonModel(1, "success", obj, sessionId);
    }

    public static JsonModel success() {
        return new JsonModel(1, "success", null);
    }

    public static JsonModel success(String msg, Object obj) {
        return new JsonModel(1, msg, obj);
    }

    public static JsonModel success(String msg, Object obj, String sessionId) {
        return new JsonModel(1, msg, obj, sessionId);
    }

    public static JsonModel success(String msg) {
        return new JsonModel(1, msg, null);
    }

    public static JsonModel error(String msg) {
        return new JsonModel(0, msg, null);
    }

    public static JsonModel error(String msg, Object obj) {
        return new JsonModel(0, msg, obj);
    }

    public static JsonModel error() {
        return new JsonModel(0, "error", null);
    }
}
