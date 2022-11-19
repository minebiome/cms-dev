package com.wangyang.pojo.entity;

import com.wangyang.pojo.entity.base.BaseCategory;
import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "1")
@Data
public class Collection extends BaseCategory {
    private String name;
    private String key;
    private String parentKey;
    private String version;

}
