package com.wangyang.weixin.service;

import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface ITemplateMsgService {
    @Async
    void sendTemplateMsg(WxMpTemplateMessage msg, String appid);

    @Async
    void sendMsgBatch(WxMpTemplateMessage form, List<String> openId);
}
