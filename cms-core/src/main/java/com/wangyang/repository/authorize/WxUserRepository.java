package com.wangyang.repository.authorize;


import com.wangyang.pojo.authorize.APIUser;
import com.wangyang.pojo.authorize.WxUser;
import com.wangyang.repository.base.AuthorizeRepository;

import java.util.List;

public interface WxUserRepository extends AuthorizeRepository<WxUser> {

    List<WxUser> findByOpenId(String openId);
}
