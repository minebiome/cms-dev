package com.wangyang.pojo.entity.base;


import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "BaseDiscuss")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")
@Data
public class BaseDiscuss extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    private Integer userId;
    private String username;
    private String email;
    @Column(name = "content", columnDefinition = "longtext")
    private String content;


    @Column(name = "original_content", columnDefinition = "longtext")
    private String originalContent;
    @Column(name = "format_content", columnDefinition = "longtext")
    private String formatContent;
}
