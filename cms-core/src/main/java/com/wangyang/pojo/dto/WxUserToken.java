package com.wangyang.pojo.dto;

import com.wangyang.pojo.authorize.LoginUser;
import com.wangyang.pojo.authorize.WxUser;
import com.wangyang.pojo.support.Token;
import lombok.Data;

@Data
public class WxUserToken extends WxUser {

    private Token  token;
}
