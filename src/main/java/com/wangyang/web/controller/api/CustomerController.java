package com.wangyang.web.controller.api;

import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.pojo.authorize.User;
import com.wangyang.pojo.entity.Customer;
import com.wangyang.pojo.params.ArticleParams;
import com.wangyang.service.authorize.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    ICustomerService customerService;


    @PostMapping
    @Anonymous
    public Customer add(@RequestBody @Valid Customer customer, HttpServletRequest request){
        return customerService.add(customer);
    }


    @GetMapping("/del/{id}")
    public Customer delUser(@PathVariable("id") Integer id){
        return customerService.delBy(id);
    }


}
