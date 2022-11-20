package com.wangyang.pojo.entity;

import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.pojo.entity.base.Content;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "2")
@Data
public class Literature extends Content {
    private String title;
    @Column(name = "literature_key")
    private String key;
    private String zoteroKey;
    private String author;
    private String url;
    private Integer categoryId;


}
