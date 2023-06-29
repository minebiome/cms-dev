package com.wangyang.pojo.entity;

import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class ArticleAttachment extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    private Integer articleId;
    private Integer templateId;
    private Integer attachmentId;
    private String path;

}
