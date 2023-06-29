package com.wangyang.pojo.entity;

import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.Column;

@Data
public class Mail extends BaseEntity {
    @Column(name = "mail_content", columnDefinition = "longtext not null")
    private String content;
    private String title;
}
