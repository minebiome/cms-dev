package com.wangyang.weixin.controller;


import com.alibaba.fastjson.JSON;
import com.wangyang.common.BaseResponse;
import com.wangyang.common.CmsConst;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.pojo.authorize.LoginUser;
import com.wangyang.pojo.authorize.WxUser;
import com.wangyang.service.authorize.IWxUserService;
import com.wangyang.weixin.util.CaptchaGenerator;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.Context;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/wx/auth")
public class WxWeb {
    @Autowired
    private CaptchaGenerator captchaGenerator;
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
                wxRedirectUri+"/wx/auth/phone",
                state);
        return "redirect:"+authUrl;
    }
//    http://192.168.10.30:8080/api/article?authUrl=/wx/auth/loginNoSave
    @GetMapping("/phone")
    @Anonymous
    public ModelAndView callLoginNoSave(@RequestParam(required = false) String code,@RequestParam(required = false) String state, ModelAndView modelAndView, HttpServletResponse response){
//        try {
        if(code!=null){
            WxUser wxUser = wxUserService.loginNoSave(code);
//            context.setVariable();
            modelAndView.addObject("wxUser",JSON.toJSON(wxUser).toString());
            modelAndView.addObject("state",state);
        }


//
//            // 设置Cookie的属性（可选）
//            user.setMaxAge(3600); // 设置过期时间为1小时
//            user.setPath("/");
//            response.addCookie(user);


//            return "redirect:"+state;
            modelAndView.setViewName(CmsConst.TEMPLATE_FILE_PREFIX+"phone");
            return modelAndView;
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
    }




    @PostMapping("/phone")
    @Anonymous
    public String phoneAdd( String captcha,  String redirect, WxUser wxUser,HttpServletResponse response,HttpServletRequest request){
        String captchaText = (String) request.getSession().getAttribute("captcha");
        LocalDateTime expirationTime = (LocalDateTime) request.getSession().getAttribute("captcha_expiration");
        LoginUser loginUser = wxUserService.login(wxUser);


        String requestURI = request.getRequestURI();
        if (captchaText != null && captchaText.equalsIgnoreCase(captcha)) {
            if (expirationTime != null && expirationTime.isAfter(LocalDateTime.now())) {
                // 验证码验证通过且未过期
                Cookie cookie = new Cookie("Authorization", loginUser.getToken());
                // 设置Cookie的属性（可选）
                cookie.setMaxAge(3600); // 设置过期时间为1小时
                cookie.setPath("/");
                response.addCookie(cookie);
                return "redirect:"+redirect;

            } else {
                // 验证码已过期
//                return "The captcha has expired.";
                return "redirect:"+requestURI;
            }
        } else {
            // 验证码验证失败
//            return "Invalid captcha.";
            return "redirect:"+requestURI;
        }


    }

    private void sendSms(String phoneNumber, String verificationCode) {
        // 实际项目中，调用短信服务商的API发送短信
        // 这里只是简单地打印验证码和手机号码
        System.out.println("发送验证码：" + verificationCode + " 到手机号码：" + phoneNumber);
    }

    @GetMapping("/smsVerification")
    @Anonymous
    @ResponseBody
    public BaseResponse smsVerification(@RequestParam String phone,HttpServletRequest request){
        LocalDateTime getExpirationTime = (LocalDateTime) request.getSession().getAttribute("captcha_expiration");
        if (getExpirationTime != null && getExpirationTime.isAfter(LocalDateTime.now())) {
            // 验证码验证通过且未过期
            throw new ObjectException("验证码已发送请稍后再试！");
        }

        LocalDateTime expirationTime = getExpirationTime();
        String verificationCode = generateVerificationCode();
        request.getSession().setAttribute("captcha", verificationCode);
        request.getSession().setAttribute("captcha_expiration", expirationTime);
        sendSms(phone, verificationCode);
        return BaseResponse.ok("验证码发送成功！");
    }
    private static final int EXPIRATION_MINUTES = 5; // 验证码有效期（分钟）
    public LocalDateTime getExpirationTime() {
        return LocalDateTime.now().plus(EXPIRATION_MINUTES, ChronoUnit.MINUTES);
    }
    private static String generateVerificationCode() {
        // 生成随机六位数验证码
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900000) + 100000;
        return Integer.toString(code);
    }
    @GetMapping("/captcha")
    @Anonymous
    public void generateCaptcha(HttpServletRequest request, HttpServletResponse response)  {
        captchaGenerator.generateCaptchaImage(request, response);
    }
//    @PostMapping("/captcha/validate")
//    public ResponseEntity<String> validateCaptcha(HttpServletRequest request, @RequestParam("captcha") String inputCaptcha) {
//        boolean isValid = captchaGenerator.validateCaptcha(request, inputCaptcha);
//
//        if (isValid) {
//            return ResponseEntity.ok("验证码正确");
//        } else {
//            return ResponseEntity.badRequest().body("验证码错误");
//        }
//    }
}
