package com.wangyang.pojo.authorize;

import lombok.Data;

/**
 * @author wangyang
 * @date 2021/6/14
 */
@Data
public class LoginUser {
    private int id;
    private String username;
    private String nickname;
    private String avatar;
    private String email;
    private Integer gender;

    private String sessionKey;
    private String token;
    private long exp;
}
