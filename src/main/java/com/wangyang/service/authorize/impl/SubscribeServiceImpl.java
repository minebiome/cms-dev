package com.wangyang.service.authorize.impl;

import com.wangyang.pojo.entity.Customer;
import com.wangyang.pojo.entity.Subscribe;
import com.wangyang.pojo.enums.CrudType;
import com.wangyang.repository.authorize.SubscribeRepository;
import com.wangyang.repository.base.AuthorizeRepository;
import com.wangyang.service.authorize.ICustomerService;
import com.wangyang.service.authorize.ISubscribeService;
import com.wangyang.service.base.AbstractAuthorizeServiceImpl;
import org.springframework.stereotype.Service;

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
}
