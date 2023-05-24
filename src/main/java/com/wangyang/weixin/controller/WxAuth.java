package com.wangyang.weixin.controller;

import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.pojo.authorize.LoginUser;
import com.wangyang.pojo.authorize.WxUser;
import com.wangyang.service.authorize.IWxUserService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/wxauth")
public class WxAuth {


    private final IWxUserService wxUserService;

    @GetMapping
    @Anonymous

    public LoginUser authGet(@RequestParam String code) {
        LoginUser loginUser = wxUserService.login(code);
        return loginUser;


//        log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,
//                timestamp, nonce, echostr);
//        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
//            throw new IllegalArgumentException("请求参数非法，请核实!");
//        }
//
//        if (!this.wxService.switchover(appid)) {
//            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
//        }
//
//        if (wxService.checkSignature(timestamp, nonce, signature)) {
//            return echostr;
//        }

//        return "非法请求";
    }
}
