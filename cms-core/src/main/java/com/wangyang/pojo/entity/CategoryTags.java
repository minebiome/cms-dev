package com.wangyang.pojo.entity;


import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class CategoryTags extends BaseEntity {
    private int categoryId;
    private int tagsId;
}
