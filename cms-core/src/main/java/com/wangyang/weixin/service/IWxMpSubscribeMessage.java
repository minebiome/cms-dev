package com.wangyang.weixin.service;

import org.springframework.scheduling.annotation.Async;

public interface IWxMpSubscribeMessage {
    @Async
    void sendSubscribeMessageMsg(String openId);
}
