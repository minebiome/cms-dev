package com.wangyang.web.controller.api;


import com.wangyang.common.exception.ObjectException;
import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.pojo.entity.Customer;
import com.wangyang.pojo.entity.Subscribe;
import com.wangyang.service.MailService;
import com.wangyang.service.authorize.ISubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/subscribe")
public class SubscribeController {

    @Autowired
    ISubscribeService subscribeService;
    @Autowired
    private MailService mailService;
    @PostMapping
    @Anonymous
    public Subscribe add(@RequestBody @Valid Subscribe subscribeInput, HttpServletRequest request){
        Subscribe serviceByEmail = subscribeService.findByEmail(subscribeInput.getEmail());
        if(serviceByEmail==null){
            Subscribe subscribe = subscribeService.add(subscribeInput);
            mailService.sendEmail(subscribe);
            return subscribe;
        }
        throw new ObjectException("您已经订阅了！");
    }

    @GetMapping("/del/{id}")
    public Subscribe delUser(@PathVariable("id") Integer id){
        return subscribeService.delBy(id);
    }
}
