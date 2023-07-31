package com.wangyang.pojo.authorize;

import lombok.*;

/**
 * @author wangyang
 * @date 2021/6/14
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUser {

    private Integer id;

    private String openId;

    private String username;

    private String nickname;

    private String avatar;

    private String email;

    private Integer gender;

    private String sessionKey;

    private String token;

    private long exp;
}
