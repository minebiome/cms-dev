package com.wangyang.service.base;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.wangyang.pojo.enums.PropertyEnum;
import com.wangyang.service.IOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("sysSmsService")
public class SmsService {

    @Value("${sms.verification-code.sign-name:西安菌佑医疗科技}")
    private String verificationCodeSignName;

    @Value("${sms.verification-code.template-code:SMS_461811033}")
    private String verificationCodeTemplateCode;

    @Value("${sms.notify.sign-name:西安菌佑医疗科技}")
    private String notifySignName;

    @Value("${sms.notify.template-code:SMS_461821050}")
    private String notifyTemplateCode;

    @Autowired
    private IOptionService optionService;

    public Boolean sendSmsNotify(String phoneNumber, Map<String, Object> params) throws Exception {
        return sendSms(phoneNumber, notifySignName, notifyTemplateCode,
                JSON.toJSONString(params));
    }

    /**
     * 发送短信验证码
     * @param phoneNumber 手机号
     * @return 是否发送成功
     */
    public Boolean sendSmsVerificationCode(String code, String phoneNumber) throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("code", code);
        return sendSms(phoneNumber, verificationCodeSignName, verificationCodeTemplateCode,
                JSON.toJSONString(params));
    }




    public Boolean sendSms(String phoneNumber, String signName, String templateCode, String templateParam) {

        String endpoint = optionService.getPropertyStringValue(PropertyEnum.END_POINT);
        String accessKey = optionService.getPropertyStringValue(PropertyEnum.ACCESS_KEY);
        String accessSecret = optionService.getPropertyStringValue(PropertyEnum.ACCESS_SECRET);


        Config config = new Config()
                .setAccessKeyId(accessKey)
                .setAccessKeySecret(accessSecret);
        config.endpoint = "dysmsapi.aliyuncs.com";
        try {
            Client client = new Client(config);
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setPhoneNumbers(phoneNumber)
                    .setTemplateParam(templateParam);
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            log.info("sms resp: {}", JSON.toJSONString(sendSmsResponse));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("短信服务发送异常, msg: " + e.getMessage());
        }





        /*AsyncClient client = null;
        try {
            StaticCredentialProvider provider = StaticCredentialProvider.create(
                    Credential.builder()
                            .accessKeyId(accessKey)
                            .accessKeySecret(accessSecret)
                            .build());
            client = AsyncClient.builder()
                    .region("cn-hangzhou")
                    .credentialsProvider(provider)
                    .overrideConfiguration(ClientOverrideConfiguration.create()
                            .setEndpointOverride("dysmsapi.aliyuncs.com"))
                    .build();
            SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                    .signName(signName)
                    .templateCode(templateCode)
                    .phoneNumbers(phoneNumber)
                    .templateParam(templateParam)
                    .build();
            CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
            SendSmsResponse sendSmsResponse = response.get();
            log.info(JSON.toJSONString(sendSmsResponse));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("短信服务发送异常, msg: " + e.getMessage());
        } finally {
            if (client != null) {
                client.close();
            }
        }*/
        return true;
    }

}
