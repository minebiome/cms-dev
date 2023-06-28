package com.wangyang.pojo.entity;


import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class TemplateChild extends BaseEntity {
    private Integer templateId;
    private Integer templateChildId;

}
