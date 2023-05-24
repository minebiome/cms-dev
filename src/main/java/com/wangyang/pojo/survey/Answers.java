package com.wangyang.pojo.survey;

import com.wangyang.pojo.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

/**
 * Answers（答案表）：存储每个用户对于每个问题的具体答案，以及该答案所属的问卷、问题和用户。
 */
@Entity
@Data
public class Answers extends BaseEntity {
}
