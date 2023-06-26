package com.wangyang.service.authorize.impl;

import com.wangyang.pojo.authorize.BaseAuthorize;
import com.wangyang.pojo.enums.CrudType;
import com.wangyang.repository.base.AuthorizeRepository;
import com.wangyang.service.base.AbstractAuthorizeServiceImpl;
import com.wangyang.service.base.IAuthorizeService;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeServiceImpl  extends AbstractAuthorizeServiceImpl<BaseAuthorize>
        implements  IAuthorizeService<BaseAuthorize>{


    private AuthorizeRepository<BaseAuthorize> authorizeAuthorizeRepository;
    public AuthorizeServiceImpl(AuthorizeRepository<BaseAuthorize> authorizeAuthorizeRepository) {
        super(authorizeAuthorizeRepository);
        this.authorizeAuthorizeRepository = authorizeAuthorizeRepository;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
