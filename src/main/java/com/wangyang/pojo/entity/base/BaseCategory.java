package com.wangyang.pojo.entity.base;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "t_category")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")
@Data
public class BaseCategory extends BaseEntity{
    private String name;
}
