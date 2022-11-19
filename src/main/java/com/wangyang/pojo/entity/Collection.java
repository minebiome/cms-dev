package com.wangyang.pojo.entity;

import com.wangyang.pojo.entity.base.BaseCategory;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "1")
@Data
public class Collection extends BaseCategory {
    private String name;
    @Column(name = "collection_key")
    private String key;
    private String parentKey;
    @Column(name = "collection_version")
    private String version;

}
