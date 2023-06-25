package com.wangyang.weixin.controller;

import com.wangyang.weixin.service.WeiXinService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/wechat/portal")
@RequiredArgsConstructor
@Slf4j
public class WxMpPortalController {
    private final WxMpService wxService;
//    protected WeiXinService getWxService() {
//        return this.wxService;
//    }
    private final WxMpMessageRouter messageRouter;

    @ResponseBody
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(@RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {
        log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }
        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "非法请求";
    }

//    @ResponseBody
//    @PostMapping(produces = "application/xml; charset=UTF-8")
//    public String post(@RequestBody String requestBody, @RequestParam("signature") String signature,
//                       @RequestParam(name = "encrypt_type", required = false) String encType,
//                       @RequestParam(name = "msg_signature", required = false) String msgSignature,
//                       @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce) {
//        log.info(
//                "\n接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}],"
//                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
//                signature, encType, msgSignature, timestamp, nonce, requestBody);
//        if (!this.wxService.checkSignature(timestamp, nonce, signature)) {
//            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
//        }
//        String out = null;
//        if (encType == null) {
//            // 明文传输的消息
//            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
//            WxMpXmlOutMessage outMessage = this.getWxService().route(inMessage);
//            if (outMessage == null) {
//                return "";
//            }
//            out = outMessage.toXml();
//        } else if ("aes".equals(encType)) {
//            // aes加密的消息
//            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody,
//                    this.getWxService().getWxMpConfigStorage(), timestamp, nonce, msgSignature);
//            log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
//            WxMpXmlOutMessage outMessage = this.getWxService().route(inMessage);
//            if (outMessage == null) {
//                return "";
//            }
//            out = outMessage.toEncryptedXml(this.getWxService().getWxMpConfigStorage());
//        }
//        log.debug("\n组装回复信息：{}", out);
//        return out;
//    }



//    @GetMapping("/qr")
//    public String qr(){
//        try {
//            WxMpQrCodeTicket ticket = wxService.getQrcodeService().qrCodeCreateLastTicket(2123);
//            File file = wxService.getQrcodeService().qrCodePicture(ticket);
//            System.out.println();
//        } catch (WxErrorException e) {
//            throw new RuntimeException(e);
//        }
//        return "";
//    }
//
@PostMapping(produces = "application/xml; charset=UTF-8")
@ApiOperation(value = "微信各类消息",notes = "公众号接入开发模式后才有效")
public String post(@RequestBody String requestBody, @RequestParam("signature") String signature,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature,
                       @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce) {
//		logger.debug("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
//						+ " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
//				openid, signature, encType, msgSignature, timestamp, nonce, requestBody);
//    this.wxService.switchoverTo(appid);
    if (!wxService.checkSignature(timestamp, nonce, signature)) {
        throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
    }

    String out = null;
    if (encType == null) {
        // 明文传输的消息
        WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
        WxMpXmlOutMessage outMessage = this.route(inMessage);
        if (outMessage == null) {
            return "";
        }

        out = outMessage.toXml();
    } else if ("aes".equalsIgnoreCase(encType)) {
        // aes加密的消息
        WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(),
                timestamp, nonce, msgSignature);
        log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
        WxMpXmlOutMessage outMessage = this.route(inMessage);
        if (outMessage == null) {
            return "";
        }

        out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
    }

    log.debug("\n组装回复信息：{}", out);
    return out;
}

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.messageRouter.route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }

        return null;
    }

}
