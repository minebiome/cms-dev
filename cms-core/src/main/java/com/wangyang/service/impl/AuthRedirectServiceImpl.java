package com.wangyang.service.impl;

import com.wangyang.common.pojo.BaseVo;
import com.wangyang.common.service.AbstractCrudService;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.pojo.entity.AuthRedirect;
import com.wangyang.repository.AuthRedirectRepository;
import com.wangyang.service.IAuthRedirectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthRedirectServiceImpl extends AbstractCrudService<AuthRedirect,AuthRedirect, BaseVo,Integer>
    implements IAuthRedirectService {
    AuthRedirectRepository authRedirectRepository;
    public AuthRedirectServiceImpl(AuthRedirectRepository authRedirectRepository) {
        super(authRedirectRepository);
        this.authRedirectRepository = authRedirectRepository;
    }

    @Override
    public List<AuthRedirect> listAll() {
        return authRedirectRepository.findAll();
    }

    @Override
    public AuthRedirect findByCurrentUrl(String currentUrl) {

        List<AuthRedirect> authRedirects = this.listAll();
        if(!authRedirects.isEmpty()){
            Map<String, AuthRedirect> authRedirectMap = ServiceUtil.convertToMap(authRedirects, AuthRedirect::getCurrentUrl);
            if(authRedirectMap.containsKey(currentUrl)){
                return authRedirectMap.get(currentUrl);
            }
        }

        return null;
    }
}
