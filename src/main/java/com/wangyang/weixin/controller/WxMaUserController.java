package com.wangyang.weixin.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/wechat/ma/user")
public class WxMaUserController {
    private final WxMaService wxMaService;

    /**
     * 登陆接口
     */
    @GetMapping("/login") //@PathVariable String appid,
    public WxMaJscode2SessionResult login( String code) {
        if (StringUtils.isBlank(code)) {
//            return "empty jscode";
            throw new RuntimeException("empty jscode");
        }

//        if (!wxMaService.switchover(appid)) {
//            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
//        }

        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            log.info(session.getSessionKey());
            log.info(session.getOpenid());
            //TODO 可以增加自己的逻辑，关联业务相关数据
            return session;
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
//            return e.toString();
            throw new RuntimeException(e.toString());
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
    }

}
