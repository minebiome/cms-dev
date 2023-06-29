package com.wangyang.service.authorize;


import com.wangyang.pojo.authorize.APIUser;
import com.wangyang.service.base.IAuthorizeService;

public interface IAPIUserService extends IAuthorizeService<APIUser> {

    APIUser findByAuthorize(String authorize);
}
