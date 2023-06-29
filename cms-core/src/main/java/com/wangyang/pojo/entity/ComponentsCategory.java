package com.wangyang.pojo.entity;

import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class ComponentsCategory  extends BaseEntity {
    private Integer categoryId;
    private Integer componentId;
    private Boolean hasArticle=true;
}
