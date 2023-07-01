package com.wangyang.weixin.service.impl;

import com.wangyang.weixin.service.IWxMpSubscribeMessage;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.subscribe.WxMpSubscribeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WxMpSubscribeMessageImpl implements IWxMpSubscribeMessage{
    @Autowired
    private WxMpService wxService;
    @Value("${cms.subscribeTemplateId}")
    private String subscribeTemplateId;


    @Override
    public String getSubscribeTemplateId() {
        return subscribeTemplateId;
    }

    @Override
    @Async
    public void sendSubscribeMessageMsg(String openId,WxSubscribeMessageParam wxSubscribeMessageParam) {
        try {
            WxMpSubscribeMessage message = WxMpSubscribeMessage.builder()
                    .templateId(subscribeTemplateId)
                    .title(wxSubscribeMessageParam.getTitle())
                    .toUser(openId)
                    .scene("1000")
                    .contentColor(wxSubscribeMessageParam.getColor())
                    .contentValue(wxSubscribeMessageParam.getContent())
                    .build();
            boolean send = this.wxService.getSubscribeMsgService().sendOnce(message);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }


}
