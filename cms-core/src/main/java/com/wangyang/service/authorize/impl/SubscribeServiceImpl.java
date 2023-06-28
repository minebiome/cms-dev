package com.wangyang.service.authorize.impl;

import com.wangyang.common.exception.ObjectException;
import com.wangyang.pojo.entity.Subscribe;
import com.wangyang.common.enums.CrudType;
import com.wangyang.repository.authorize.SubscribeRepository;
import com.wangyang.service.MailService;
import com.wangyang.service.authorize.ISubscribeService;
import com.wangyang.service.base.AbstractAuthorizeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class SubscribeServiceImpl extends AbstractAuthorizeServiceImpl<Subscribe>
        implements ISubscribeService {

    @Autowired
    private MailService mailService;
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
    @Async
    public Subscribe add(Subscribe subscribeInput) {
        Subscribe serviceByEmail = findByEmail(subscribeInput.getEmail());
        if(serviceByEmail==null){
            Subscribe subscribe = super.add(subscribeInput);
            mailService.sendEmail(subscribe);
            return subscribe;
        }
        throw new ObjectException("您已经订阅了！");
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
