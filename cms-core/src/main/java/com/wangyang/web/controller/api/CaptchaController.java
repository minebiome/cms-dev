package com.wangyang.web.controller.api;

import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.weixin.util.CaptchaGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {
    @Autowired
    private CaptchaGenerator captchaGenerator;
    @GetMapping("/captcha")
    @Anonymous
    public void generateCaptcha(HttpServletRequest request, HttpServletResponse response)  {
        captchaGenerator.generateCaptchaImage(request, response);
    }
}
