package com.wangyang.service;

import com.wangyang.common.pojo.BaseVo;
import com.wangyang.pojo.entity.AuthRedirect;
import com.wangyang.service.base.ICrudService;

import java.util.List;

public interface IAuthRedirectService extends ICrudService<AuthRedirect,AuthRedirect, BaseVo,Integer> {
    AuthRedirect addUniqueCurrentUrl(AuthRedirect authRedirectInput);

    AuthRedirect findByCurrentUrl(String currentUrl);

}
