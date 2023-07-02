package com.wangyang.service.authorize;

import com.wangyang.pojo.authorize.LoginUser;
import com.wangyang.pojo.authorize.User;
import com.wangyang.pojo.authorize.WxUser;
import com.wangyang.pojo.dto.WxUserToken;
import com.wangyang.service.base.IAuthorizeService;

public interface IWxUserService  extends IAuthorizeService<WxUser> {
    WxUserToken loginWx(String code);

    LoginUser loginMp(String code);

    WxUser loginNoSave(String code);

    LoginUser login(WxUser inputWxUser);

    WxUser findByPhoneId(String phone);

    LoginUser loginMa(String code);
}
