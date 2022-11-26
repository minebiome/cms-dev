package com.wangyang.pojo.entity.base;

import lombok.Data;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

//@Entity(name = "t_base_category")
//@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")
@Data
@MappedSuperclass
public class BaseCategory extends BaseEntity{
    private String name;
}
