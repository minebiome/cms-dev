package com.wangyang.weixin.controller;


import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.pojo.authorize.LoginUser;
import com.wangyang.service.authorize.IWxUserService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller

@RequestMapping("/wx/auth")
public class WxWeb {
    @Autowired
    private  IWxUserService wxUserService;
    @Autowired
    private   WxMpConfigStorage wxMpConfigStorage;
    @Value("${cms.wxRedirectUri}")
    private String wxRedirectUri;
//    http://192.168.0.178:8080/wx/auth?state=/login.html
    private  static String  authUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    @GetMapping
    @Anonymous
    public String auth(@RequestParam(required = false) String state){
        authUrl = authUrl.replace("APPID",wxMpConfigStorage.getAppId());
        authUrl = authUrl.replace("REDIRECT_URI",wxRedirectUri);
        if(state!=null){
            authUrl = authUrl.replace("STATE",state);
        }
        return "redirect:"+authUrl;
    }
    @GetMapping("/call")
    @Anonymous
    public String authLogin(@RequestParam String code, @RequestParam String state, HttpServletResponse response){
        LoginUser loginUser = wxUserService.login(code);
        Cookie cookie = new Cookie("Authorization", loginUser.getToken());
        // 设置Cookie的属性（可选）
        cookie.setMaxAge(3600); // 设置过期时间为1小时
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:"+state;
    }
}
