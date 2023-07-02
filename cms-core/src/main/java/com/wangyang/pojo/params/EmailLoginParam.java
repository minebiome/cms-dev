package com.wangyang.pojo.params;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class EmailLoginParam {
    @NotEmpty(message = "邮箱不能为空！")
    private String email;
    @NotEmpty(message = "验证码不能为空！")
    private String captcha;
}
