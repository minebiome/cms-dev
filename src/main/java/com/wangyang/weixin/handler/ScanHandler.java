package com.wangyang.weixin.handler;

import com.wangyang.weixin.service.IMsgReplyService;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ScanHandler extends AbstractHandler {
    @Autowired
    IMsgReplyService msgReplyService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) {
        //扫码事件处理
        this.logger.info("用户扫描带参二维码 OPENID: " + wxMpXmlMessage.getFromUser());
//        String appid = WxMpConfigStorageHolder.get();
        msgReplyService.tryAutoReply( true, wxMpXmlMessage.getFromUser(), wxMpXmlMessage.getEventKey());

        return null;
    }
}
