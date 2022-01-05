package com.wangyang.service.impl;


import com.wangyang.pojo.authorize.APIUser;
import com.wangyang.repository.ApiUserRepository;
import com.wangyang.service.IAPIUserService;
import com.wangyang.service.base.BaseAuthorizeServiceImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class APIUserServiceImpl extends BaseAuthorizeServiceImpl<APIUser>
        implements IAPIUserService {


    private  final ApiUserRepository apiUserRepository;

    public APIUserServiceImpl(ApiUserRepository apiUserRepository) {
        super(apiUserRepository);
        this.apiUserRepository=apiUserRepository;
    }

    @Override
    public APIUser findByAuthorize(String authorize) {
        List<APIUser> apiUserList = apiUserRepository.findAll(new Specification<APIUser>() {
            @Override
            public Predicate toPredicate(Root<APIUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("authorize"),authorize)).getRestriction();
            }
        });
        return apiUserList.size()==0?null:apiUserList.get(0);
    }
}
