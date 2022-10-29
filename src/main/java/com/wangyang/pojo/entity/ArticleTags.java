package com.wangyang.pojo.entity;

import com.wangyang.pojo.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class ArticleTags extends BaseEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    private int articleId;
    private int tagsId;



}
