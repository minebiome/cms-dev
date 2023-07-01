package com.wangyang.weixin.service;

import com.wangyang.weixin.service.impl.WxSubscribeMessageParam;
import org.springframework.scheduling.annotation.Async;

public interface IWxMpSubscribeMessage {
    String getSubscribeTemplateId();

    @Async
    void sendSubscribeMessageMsg(String openId, WxSubscribeMessageParam wxSubscribeMessageParam);
}
