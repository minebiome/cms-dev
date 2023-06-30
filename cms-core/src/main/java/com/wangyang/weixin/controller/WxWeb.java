package com.wangyang.weixin.controller;


import com.alibaba.fastjson.JSON;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
//    private  static String  authUrl = ;
    @GetMapping("login")
    @Anonymous
    public String login(@RequestParam(required = false) String state){
//        authUrl = authUrl.replace("APPID",);
//        authUrl = authUrl.replace("REDIRECT_URI",wxRedirectUri);
        if(state==null){
            state="/";
        }
        String authUrl = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect",
                wxMpConfigStorage.getAppId(),
                wxRedirectUri+"/wx/auth/callLogin",
                state);
        return "redirect:"+authUrl;
    }
    @GetMapping("/callLogin")
    @Anonymous
    public String callLogin(@RequestParam String code, @RequestParam String state, HttpServletResponse response){
        LoginUser loginUser = wxUserService.login(code);
        Cookie cookie = new Cookie("Authorization", loginUser.getToken());
        // 设置Cookie的属性（可选）
        cookie.setMaxAge(3600); // 设置过期时间为1小时
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:"+state;
    }




    @GetMapping("loginNoSave")
    @Anonymous
    public String loginNoSave(@RequestParam(required = false) String state){
//        authUrl = authUrl.replace("APPID",);
//        authUrl = authUrl.replace("REDIRECT_URI",wxRedirectUri);
        if(state==null){
            state="/";
        }
        String authUrl = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect",
                wxMpConfigStorage.getAppId(),
                wxRedirectUri+"/wx/auth/callLoginNoSave",
                state);
        return "redirect:"+authUrl;
    }
    @GetMapping("/callLoginNoSave")
    @Anonymous
    public String callLoginNoSave(@RequestParam String code, @RequestParam String state, HttpServletResponse response){
        try {
            LoginUser loginUser = wxUserService.loginNoSave(code);
            Cookie cookie = new Cookie("Authorization", loginUser.getToken());
            // 设置Cookie的属性（可选）
            cookie.setMaxAge(3600); // 设置过期时间为1小时
            cookie.setPath("/");
            response.addCookie(cookie);
            String encodeCookie = URLEncoder.encode(JSON.toJSON(loginUser).toString(),"utf-8");
            Cookie user = new Cookie("User",encodeCookie );

            // 设置Cookie的属性（可选）
            user.setMaxAge(3600); // 设置过期时间为1小时
            user.setPath("/");
            response.addCookie(user);


            return "redirect:"+state;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
