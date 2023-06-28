package com.wangyang.pojo.entity;

import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ComponentsArticle extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    private Integer articleId;
    private Integer componentId;
}
