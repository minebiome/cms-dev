package com.wangyang.weixin.controller;


import com.wangyang.pojo.annotation.Anonymous;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wx/auth")
public class WxWeb {

    private  static String  authUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2623bacf41ef1be9&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
    @GetMapping
    @Anonymous
    public String doGet(){
        authUrl = authUrl.replace("REDIRECT_URI","");
        return "redirect:"+authUrl;
    }
    @PostMapping
    @Anonymous
    public String doPost(){
        return "aaaaaaaaaaaaa";
    }
}
