package com.wangyang.pojo.params;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class WxPhoneParam {
    @NotEmpty(message = "验证码不能为空！")
    private String captcha;

    private String redirect;
    @NotEmpty(message = "微信ID不能为空！")
    private String openId;
    private String roleEn;
    private Integer gender;
    private String username;
//    @NotEmpty(message = "nickname不能为空！")
    private String nickname;
    private String avatar;
    private String email;
    @NotEmpty(message = "电话不能为空！")
    private String phone;

    private String currentUrl;
}
