package com.wangyang.util;

/**
 * @author wangyang
 * @date 2021/6/13
 */
public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super();
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
