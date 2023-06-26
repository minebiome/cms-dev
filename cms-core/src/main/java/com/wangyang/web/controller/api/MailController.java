package com.wangyang.web.controller.api;


import com.wangyang.pojo.authorize.BaseAuthorize;
import com.wangyang.pojo.entity.Mail;
import com.wangyang.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    MailService mailService;


    @PostMapping
    public List<BaseAuthorize> pushMail(@RequestBody  Mail mailInput){
        List<BaseAuthorize> baseAuthorizes= mailService.sendEmail(mailInput);
        return baseAuthorizes;
    }
}
