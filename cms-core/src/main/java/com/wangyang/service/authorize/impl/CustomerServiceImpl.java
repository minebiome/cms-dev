package com.wangyang.service.authorize.impl;

import com.wangyang.pojo.entity.Customer;
import com.wangyang.common.enums.CrudType;
import com.wangyang.repository.authorize.CustomerRepository;
import com.wangyang.service.MailService;
import com.wangyang.service.authorize.ICustomerService;
import com.wangyang.service.base.AbstractAuthorizeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends AbstractAuthorizeServiceImpl<Customer>
        implements ICustomerService {
    @Autowired
    private MailService mailService;
    private CustomerRepository customerRepository;
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        super(customerRepository);
        this.customerRepository=customerRepository;
    }

    @Override
    @Async
    public Customer add(Customer customer) {
        Customer customer1 = super.add(customer);

        mailService.sendEmail(customer);
        return customer1;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }




}
