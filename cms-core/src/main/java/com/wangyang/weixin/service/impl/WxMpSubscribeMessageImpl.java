package com.wangyang.weixin.service.impl;

import com.wangyang.weixin.service.IWxMpSubscribeMessage;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.subscribe.WxMpSubscribeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WxMpSubscribeMessageImpl implements IWxMpSubscribeMessage{
    @Autowired
    private WxMpService wxService;

    @Override
    @Async
    public void sendSubscribeMessageMsg(String openId,String templateId) {
        try {
            WxMpSubscribeMessage message = WxMpSubscribeMessage.builder()
                    .templateId(templateId)
                    .title("weixin test")
                    .toUser(openId)
                    .scene("1000")
                    .contentColor("#FF0000")
                    .contentValue("Send subscribe message test")
                    .build();
            boolean send = this.wxService.getSubscribeMsgService().sendOnce(message);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }


}
