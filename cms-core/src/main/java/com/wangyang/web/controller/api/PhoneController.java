package com.wangyang.web.controller.api;


import com.alibaba.fastjson.JSON;
import com.wangyang.common.BaseResponse;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.pojo.authorize.LoginUser;
import com.wangyang.pojo.authorize.UserDetailDTO;
import com.wangyang.pojo.params.PhoneLoginParam;
import com.wangyang.pojo.support.Token;
import com.wangyang.service.authorize.IUserService;
import com.wangyang.service.base.SmsService;
import com.wangyang.util.TokenProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/phone")
public class PhoneController {
    @Autowired
    IUserService userService;
    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    SmsService smsService;

    private void sendSms(String phoneNumber, String verificationCode) {
        // 实际项目中，调用短信服务商的API发送短信
        // 这里只是简单地打印验证码和手机号码
        System.out.println("发送验证码：" + verificationCode + " 到手机号码：" + phoneNumber);
        try {
            smsService.sendSmsVerificationCode(verificationCode, phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("手机验证码发送失败，失败原因: " + e.getMessage());
        }
    }

    @GetMapping("/smsVerification")
    @Anonymous
    @ResponseBody
    public BaseResponse smsVerification(@RequestParam String phone, HttpServletRequest request){
        if(phone==null || phone.equals("")){
            throw new ObjectException("手机号码不能为空！");
        }
        LocalDateTime getExpirationTime = (LocalDateTime) request.getSession().getAttribute("captcha_expiration");
        if (getExpirationTime != null && getExpirationTime.isAfter(LocalDateTime.now())) {
            // 验证码验证通过且未过期
            throw new ObjectException("验证码已发送请稍后再试！");
        }

        LocalDateTime expirationTime = CMSUtils.getExpirationTime();
        String verificationCode = CMSUtils.generateVerificationCode();
        request.getSession().setAttribute("captcha", verificationCode);
        request.getSession().setAttribute("phone_number", phone);
        request.getSession().setAttribute("captcha_expiration", expirationTime);
        sendSms(phone, verificationCode);
        return BaseResponse.ok("验证码发送成功！");
    }
    @PostMapping("/loginPhone")
    @Anonymous
    @ResponseBody
    public LoginUser loginPhone(@Valid @RequestBody PhoneLoginParam phoneLoginParam, HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {


        String captchaText = (String) request.getSession().getAttribute("captcha");
        String phoneNumber = (String) request.getSession().getAttribute("phone_number");
//        String requestURI = request.getRequestURI();
        if (captchaText != null && captchaText.equalsIgnoreCase(phoneLoginParam.getCaptcha())
                && StringUtils.isNotBlank(phoneNumber) && phoneNumber.equals(phoneLoginParam.getPhone())) {
            LocalDateTime expirationTime = (LocalDateTime) request.getSession().getAttribute("captcha_expiration");

            if (expirationTime != null && expirationTime.isAfter(LocalDateTime.now())) {
                UserDetailDTO user = userService.loginPhone(phoneLoginParam.getPhone());
                LoginUser loginUser = new LoginUser();
                BeanUtils.copyProperties(user,loginUser);
                Token token = tokenProvider.generateToken(user);
                loginUser.setToken(token.getToken());
//                BeanUtils.copyProperties(wxPhoneParam, wxUser);
//                LoginUser loginUser = wxUserService.login(wxUser);
                // 验证码验证通过且未过期
                Cookie cookie = new Cookie("Authorization", loginUser.getToken());
                // 设置Cookie的属性（可选）
                cookie.setMaxAge(3600); // 设置过期时间为1小时
                cookie.setPath("/");
                response.addCookie(cookie);
                try {
                    String encodeCookie = URLEncoder.encode(JSON.toJSON(loginUser).toString(),"utf-8");
                    Cookie userCookie = new Cookie("wxUser", encodeCookie);
                    userCookie.setMaxAge(3600); // 设置过期时间为1小时
                    userCookie.setPath("/");
                    response.addCookie(userCookie);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

//                return "redirect:"+wxPhoneParam.getRedirect();
                return loginUser;
            } else {
                throw  new ObjectException("验证码已过期！");

            }
        } else {
            throw  new ObjectException("验证码不正确！");
        }

    }
}
