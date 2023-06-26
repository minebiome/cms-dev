package com.wangyang.weixin.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import com.wangyang.pojo.annotation.Anonymous;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/wechat/ma")
@Slf4j
public class MiniappController {
    private final WxMaService wxMaService;
    private final WxMaMessageRouter wxMaMessageRouter;

    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(//@PathVariable String appid,
                          @RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {
        log.info("\n接收到来自微信服务器的认证消息：signature = [{}], timestamp = [{}], nonce = [{}], echostr = [{}]",
                signature, timestamp, nonce, echostr);

        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

//        if (!wxMaService.switchover(appid)) {
//            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
//        }

        if (wxMaService.checkSignature(timestamp, nonce, signature)) {
            WxMaConfigHolder.remove();//清理ThreadLocal
            return echostr;
        }
        WxMaConfigHolder.remove();//清理ThreadLocal
        return "非法请求";
    }



}
