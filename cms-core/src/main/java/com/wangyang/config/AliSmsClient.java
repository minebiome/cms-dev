package com.wangyang.config;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teautil.models.RuntimeOptions;

import java.util.HashMap;
import java.util.Map;

public class AliSmsClient {

    private final Client client;
    private final SendSmsRequest request;

    private final String verificationCodeSignName;

    private final String verificationCodeTemplateCode;

    private final String notifySignName;

    private final String notifyTemplateCode;

    public AliSmsClient(Client client, SendSmsRequest request,
                        String verificationCodeSignName, String verificationCodeTemplateCode,
                        String notifySignName, String notifyTemplateCode) {
        this.client = client;
        this.request = request;
        this.verificationCodeSignName = verificationCodeSignName;
        this.verificationCodeTemplateCode = verificationCodeTemplateCode;
        this.notifySignName = notifySignName;
        this.notifyTemplateCode = notifyTemplateCode;
    }

    public Boolean sendSmsNotify(String phoneNumber, Map<String, Object> params) throws Exception {
        return sendSms(notifySignName, notifyTemplateCode,
                JSON.toJSONString(params), phoneNumber);
    }

    /**
     * 发送短信验证码
     * @param phoneNumber 手机号
     * @return 是否发送成功
     */
    public Boolean sendSmsVerificationCode(String code, String phoneNumber) throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("code", code);
        return sendSms(verificationCodeSignName, verificationCodeTemplateCode,
                JSON.toJSONString(params), phoneNumber);
    }

    private Boolean sendSms(String signName, String templateCode, String templateParam, String phoneNumbers) throws Exception {
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setTemplateParam(templateParam);
        request.setPhoneNumbers(phoneNumbers);
        RuntimeOptions runtime = new RuntimeOptions();
        SendSmsResponse response = null;
        try {
            response = client.sendSmsWithOptions(request, runtime);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("短信发送失败");
        }
    }

}
