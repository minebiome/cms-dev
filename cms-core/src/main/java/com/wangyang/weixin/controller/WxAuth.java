package com.wangyang.weixin.controller;

import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.pojo.annotation.WxRole;
import com.wangyang.pojo.authorize.LoginUser;
import com.wangyang.weixin.pojo.WxJsapiSignatureParam;
import com.wangyang.service.authorize.IWxUserService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.*;

import java.util.TreeMap;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/wxauth")
public class WxAuth {


    private final  WxMpService wxService;
    private final IWxUserService wxUserService;

    @GetMapping
    @Anonymous
    public LoginUser authGet(@RequestParam String code) throws WxErrorException {
        LoginUser loginUser = wxUserService.loginMp(code);
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


    @PostMapping("/createJsapiSignature")
    @WxRole
    public WxJsapiSignature createJsapiSignature(@RequestBody WxJsapiSignatureParam wxJsapiSignatureParam){
        try {
            WxJsapiSignature jsapiSignature = wxService.createJsapiSignature(wxJsapiSignatureParam.getUrl());
            System.out.println();

            return jsapiSignature;
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getShareSignature")
    @Anonymous
    public WxJsapiSignature getShareSignature(WxJsapiSignatureParam wxJsapiSignatureParam){
        try {
            WxJsapiSignature jsapiSignature = wxService.createJsapiSignature(wxJsapiSignatureParam.getUrl());
            System.out.println();
            String jsapiTicket = wxService.getJsapiTicket();

            return jsapiSignature;
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }
//    @GetMapping("/getShareSignature")
////    @ApiOperation(value = "获取微信分享的签名配置",notes = "微信公众号添加了js安全域名的网站才能加载微信分享")
//    public R getShareSignature(HttpServletRequest request, HttpServletResponse response) throws WxErrorException {
////        wxService.switchoverTo(appid);
//        // 1.拼接url（当前网页的URL，不包含#及其后面部分）
//        String wxShareUrl = request.getHeader(Constant.WX_CLIENT_HREF_HEADER);
//        if (!StringUtils.hasText(wxShareUrl)) {
//            return R.error("header中缺少"+Constant.WX_CLIENT_HREF_HEADER+"参数，微信分享加载失败");
//        }
//        wxShareUrl = wxShareUrl.split("#")[0];
//        Map<String, String> wxMap = new TreeMap<>();
//        String wxNoncestr = UUID.randomUUID().toString();
//        String wxTimestamp = (System.currentTimeMillis() / 1000) + "";
//        wxMap.put("noncestr", wxNoncestr);
//        wxMap.put("timestamp", wxTimestamp);
//        wxMap.put("jsapi_ticket", wxService.getJsapiTicket());
//        wxMap.put("url", wxShareUrl);
//
//        // 加密获取signature
//        StringBuilder wxBaseString = new StringBuilder();
//        wxMap.forEach((key, value) -> wxBaseString.append(key).append("=").append(value).append("&"));
//        String wxSignString = wxBaseString.substring(0, wxBaseString.length() - 1);
//        // signature
//        String wxSignature = SHA1Util.sha1(wxSignString);
//        Map<String, String> resMap = new TreeMap<>();
//        resMap.put("appId", appid);
//        resMap.put("wxTimestamp", wxTimestamp);
//        resMap.put("wxNoncestr", wxNoncestr);
//        resMap.put("wxSignature", wxSignature);
//        return R.ok().put(resMap);
//    }
}
