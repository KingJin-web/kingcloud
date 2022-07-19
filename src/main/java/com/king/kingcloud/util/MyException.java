package com.king.kingcloud.util;

/**
 * @author: King
 * @project: kingcloud
 * @date: 2022年07月18日 19:59
 * @description:
 */
public class MyException extends RuntimeException {
    private static final long serialVersionUID = 123314241415531312L;

    public MyException() {
        super();
    }

    public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyException(String message) {
        super(message);
    }

    public MyException(Throwable cause) {
        super(cause);
    }
}