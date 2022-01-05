package com.wangyang.service;


import com.wangyang.pojo.authorize.APIUser;
import com.wangyang.service.base.IBaseAuthorizeService;

public interface IAPIUserService extends IBaseAuthorizeService<APIUser> {

    APIUser findByAuthorize(String authorize);
}
