package com.wangyang.weixin.controller;


import com.alibaba.fastjson.JSON;
import com.wangyang.common.BaseResponse;
import com.wangyang.common.CmsConst;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.pojo.annotation.WxRole;
import com.wangyang.pojo.authorize.LoginUser;
import com.wangyang.pojo.authorize.WxUser;
import com.wangyang.pojo.params.WxPhoneParam;
import com.wangyang.service.authorize.IWxUserService;
import com.wangyang.weixin.service.ITemplateMsgService;
import com.wangyang.weixin.service.IWxMpSubscribeMessage;
import com.wangyang.weixin.util.CaptchaGenerator;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.util.WxMpConfigStorageHolder;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.Context;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/wx/auth")
public class WxWeb {
    @Autowired
    private CaptchaGenerator captchaGenerator;
    @Autowired
    private  IWxUserService wxUserService;
    @Autowired
    private   WxMpConfigStorage wxMpConfigStorage;
    @Autowired
    ITemplateMsgService templateMsgService;

    @Autowired
    IWxMpSubscribeMessage wxMpSubscribeMessage;



    @Value("${cms.templateId}")
    String templateId;

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




//    wxMpSubscribeMessage.sendSubscribeMessageMsg(wxUser.getOpenId());
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


//   response.sendRedirect(authUrl + "?state=" + requestURI + "?authUrl=" + authUrl);

    @GetMapping("/loginNoSave/{viewName}")
    @Anonymous
    public String loginNoSave(@PathVariable String viewName,@RequestParam(required = false) String state){
//        authUrl = authUrl.replace("APPID",);
//        authUrl = authUrl.replace("REDIRECT_URI",wxRedirectUri);
        if(state==null){
            state="/";
        }
        String authUrl = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect",
                wxMpConfigStorage.getAppId(),
                wxRedirectUri+"/wx/auth/phone/"+viewName,
                state);
        return "redirect:"+authUrl;
    }
//    http://192.168.0.178:8080/api/article?authUrl=/wx/auth/loginNoSave/aaaa
    @GetMapping("/phone/{viewName}")
    @Anonymous
    public ModelAndView callLoginNoSave(@PathVariable String viewName,@RequestParam(required = false) String code,@RequestParam(required = false) String state, ModelAndView modelAndView, HttpServletResponse response){
//        try {
        if(code!=null){
            WxUser wxUser = wxUserService.loginNoSave(code);


            String appid = WxMpConfigStorageHolder.get();
            List<WxMpTemplateData> data  = new ArrayList<>();
            data.add(new WxMpTemplateData("first","模板消息测试"));
            data.add(new WxMpTemplateData("keywords1","xxxxx"));
            data.add(new WxMpTemplateData("keywords2","xxxxx"));
            data.add(new WxMpTemplateData("remark","点击查看消息详情"));
            if(templateId!=null && !"".equals(templateId)){
                WxMpTemplateMessage wxMpTemplateMessage = WxMpTemplateMessage.builder()
                        .templateId(templateId)
                        .url("https://www.yuque.com/nifury/wx/cyku5l")
                        .toUser(wxUser.getOpenId())
                        .data(data)
                        .build();
                templateMsgService.sendTemplateMsg(wxMpTemplateMessage,appid);

            }
//            if(subscribeTemplateId!=null && !"".equals(subscribeTemplateId)){
//
//            }


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
            modelAndView.setViewName(CMSUtils.phoneAuth()+"/"+viewName);
            return modelAndView;
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
    }



    @PostMapping("/phone")
    @Anonymous
    @ResponseBody
    public LoginUser phoneAdd(@Valid @RequestBody  WxPhoneParam wxPhoneParam, HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {



        WxUser wxUser = new WxUser();
        BeanUtils.copyProperties(wxPhoneParam, wxUser);

        String captchaText = (String) request.getSession().getAttribute("captcha");
        LocalDateTime expirationTime = (LocalDateTime) request.getSession().getAttribute("captcha_expiration");
        LoginUser loginUser = wxUserService.login(wxUser);


        String requestURI = request.getRequestURI();
        if (captchaText != null && captchaText.equalsIgnoreCase(wxPhoneParam.getCaptcha())) {
            if (expirationTime != null && expirationTime.isAfter(LocalDateTime.now())) {
                // 验证码验证通过且未过期
                Cookie cookie = new Cookie("Authorization", loginUser.getToken());
                // 设置Cookie的属性（可选）
                cookie.setMaxAge(3600); // 设置过期时间为1小时
                cookie.setPath("/");
                response.addCookie(cookie);
//                return "redirect:"+wxPhoneParam.getRedirect();
                return loginUser;
            } else {
                throw  new ObjectException("验证码已过期！");
                // 验证码已过期
//                return "The captcha has expired.";
//                return wxPhoneParam.getCurrentUrl()+"?state="+wxPhoneParam.getRedirect();
            }
        } else {
            throw  new ObjectException("验证码不正确！");
            // 验证码验证失败
//            return "Invalid captcha.";
//            request.getRequestDispatcher(wxPhoneParam.getCurrentUrl()).forward(request,response);
//            return "redirect:"+wxPhoneParam.getCurrentUrl();
//            return wxPhoneParam.getCurrentUrl()+"?state="+wxPhoneParam.getRedirect();
        }




    }
//    http://192.168.0.178:8080/wx/auth/subscribeMsg?redirect=/&reserved=authUrl=/wx/auth/loginNoSave/aaaa

    @GetMapping("/subscribeMsg")
    @Anonymous
    public String subscribeMsg(@RequestParam(required = false) String reserved,String redirect){
//        authUrl = authUrl.replace("APPID",);
//        authUrl = authUrl.replace("REDIRECT_URI",wxRedirectUri);
        if(reserved==null){
            reserved="/";
        }
        String authUrl = String.format("https://mp.weixin.qq.com/mp/subscribemsg?action=get_confirm&appid=%s&scene=1000&template_id=%s&redirect_url=%s&reserved=%s#wechat_redirect",
                wxMpConfigStorage.getAppId(),
                wxMpSubscribeMessage.getSubscribeTemplateId(),
                wxRedirectUri+"/"+redirect,
                reserved);
        return "redirect:"+authUrl;
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
        if(phone==null || phone.equals("")){
            throw new ObjectException("手机号码不能为空！");
        }
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
    private static final int EXPIRATION_MINUTES = 1; // 验证码有效期（分钟）
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

    @GetMapping("/testWxAuth1")
    @WxRole
    public String testWxAuth1(){
        return CmsConst.TEMPLATE_FILE_PREFIX+"testWxAuth1";
    }
    @GetMapping("/testWxAuth2")
    @WxRole
    public String testWxAuth2(){
        return CmsConst.TEMPLATE_FILE_PREFIX+"testWxAuth2";
    }
}
