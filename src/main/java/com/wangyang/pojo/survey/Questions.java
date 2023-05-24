package com.wangyang.pojo.survey;

import com.wangyang.pojo.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

/**
 * Questions（问题表）：存储每个问卷中的问题和选项，包括问题的类型、题干、选项内容、顺序等。
 */
@Entity
@Data
public class Questions extends BaseEntity {
}
