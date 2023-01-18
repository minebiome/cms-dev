package com.wangyang.pojo.entity;

import com.wangyang.pojo.authorize.BaseAuthorize;
import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "4")
@Data
public class Subscribe extends BaseAuthorize {
}
