package com.wangyang.pojo.survey;

import com.wangyang.pojo.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

/**
 * Surveys（问卷表）：存储每个问卷的基本信息，如问卷名称、描述、创建时间等。
 */
@Entity
@Data
public class Surveys extends BaseEntity {
}
