package com.wangyang.pojo.entity;

import com.wangyang.pojo.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "t_literature")
@Data
public class Literature extends BaseEntity {
    private String title;
    @Column(name = "literature_key")
    private String key;
    private String author;
    private String url;
}
