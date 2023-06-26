package com.wangyang.web.controller.api;

import com.wangyang.pojo.authorize.BaseAuthorize;
import com.wangyang.service.base.IAuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authorize")
public class AuthorizeServiceController {

    @Autowired
    @Qualifier("authorizeServiceImpl")
    IAuthorizeService authorizeService;
}
