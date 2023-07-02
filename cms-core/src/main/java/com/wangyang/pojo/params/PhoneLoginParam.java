package com.wangyang.pojo.params;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PhoneLoginParam {
    @NotEmpty(message = "手机不能为空！")
    private String phone;
    @NotEmpty(message = "验证码不能为空！")
    private String captcha;
}
