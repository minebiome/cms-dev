package com.wangyang.service.authorize.impl;

import com.wangyang.pojo.authorize.User;
import com.wangyang.pojo.authorize.WxUser;
import com.wangyang.pojo.enums.CrudType;
import com.wangyang.repository.authorize.WxUserRepository;
import com.wangyang.service.authorize.IUserService;
import com.wangyang.service.authorize.IWxUserService;
import com.wangyang.service.base.AbstractAuthorizeServiceImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class WxUserServiceImpl  extends AbstractAuthorizeServiceImpl<WxUser>
        implements IWxUserService {

    private final WxUserRepository wxUserRepository;

    public WxUserServiceImpl(WxUserRepository wxUserRepository){
        super(wxUserRepository);
        this.wxUserRepository=wxUserRepository;
    }





    public WxUser findBYOpenId(String openId){
        List<WxUser> wxUsers = wxUserRepository.findAll(new Specification<WxUser>() {
            @Override
            public Predicate toPredicate(Root<WxUser> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return null;
            }
        });
        if(wxUsers.size()==1){
            return wxUsers.get(0);
        }
        return null;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
