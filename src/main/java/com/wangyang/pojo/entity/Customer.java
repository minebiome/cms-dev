package com.wangyang.pojo.entity;

import com.wangyang.pojo.authorize.BaseAuthorize;
import com.wangyang.pojo.authorize.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "3")
@Data
public class Customer extends BaseAuthorize {
    private String unit;
    @Column(name = "customer_content", columnDefinition = "longtext")
    private String content;
}
