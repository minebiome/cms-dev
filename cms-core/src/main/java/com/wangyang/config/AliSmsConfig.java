package com.wangyang.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;
import com.wangyang.pojo.enums.PropertyEnum;
import com.wangyang.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云短信服务配置
 */
@Configuration
public class AliSmsConfig {

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

    @Bean
    public AliSmsClient aliSms() {
        return new AliSmsClient(createClient(), sendSmsRequest(),
                verificationCodeSignName, verificationCodeTemplateCode,
                notifySignName, notifyTemplateCode);
    }

    private SendSmsRequest sendSmsRequest() {
        SendSmsRequest request = new SendSmsRequest();
        return request;
    }

    private Client createClient() {
        String endpoint = optionService.getPropertyStringValue(PropertyEnum.END_POINT);
        String accessKey = optionService.getPropertyStringValue(PropertyEnum.ACCESS_KEY);
        String accessSecret = optionService.getPropertyStringValue(PropertyEnum.ACCESS_SECRET);
        Config config = new Config()
                .setAccessKeyId(accessKey)
                .setAccessKeySecret(accessSecret);
        // 访问的域名
        config.endpoint = endpoint;
        Client client = null;
        try {
            client = new Client(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建sms客户端失败！");
        }
        return client;
    }

}
