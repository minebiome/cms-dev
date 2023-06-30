package com.wangyang.service.authorize;

import com.wangyang.pojo.authorize.LoginUser;
import com.wangyang.pojo.authorize.User;
import com.wangyang.pojo.authorize.WxUser;
import com.wangyang.service.base.IAuthorizeService;

public interface IWxUserService  extends IAuthorizeService<WxUser> {
    LoginUser login(String code);

    LoginUser loginNoSave(String code);
}
