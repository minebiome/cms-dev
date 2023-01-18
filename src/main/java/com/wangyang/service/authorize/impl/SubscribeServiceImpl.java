package com.wangyang.service.authorize.impl;

import com.wangyang.common.exception.ObjectException;
import com.wangyang.pojo.entity.Subscribe;
import com.wangyang.pojo.enums.CrudType;
import com.wangyang.repository.authorize.SubscribeRepository;
import com.wangyang.service.authorize.ISubscribeService;
import com.wangyang.service.base.AbstractAuthorizeServiceImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class SubscribeServiceImpl extends AbstractAuthorizeServiceImpl<Subscribe>
        implements ISubscribeService {


    private SubscribeRepository subscribeRepository;
    public SubscribeServiceImpl(SubscribeRepository subscribeRepository) {
        super(subscribeRepository);
        this.subscribeRepository =subscribeRepository;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }


    @Override
    public Subscribe findByEmail(String email){
        List<Subscribe> subscribes = subscribeRepository.findAll(new Specification<Subscribe>() {
            @Override
            public Predicate toPredicate(Root<Subscribe> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("email"), email)).getRestriction();
            }
        });
        if(subscribes.size()==0){
            return null;
        }
        return subscribes.get(0);
    }
}
