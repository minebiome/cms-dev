package com.wangyang.pojo.entity.base;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "BaseTemplate")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")
@Data
public class BaseTemplate extends  BaseEntity{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    private String name;
    private String enName;
    private String description;
    @Column(columnDefinition = "bit(1) default false")
    private Boolean status=false;
    @Column(name = "template_value", columnDefinition = "longtext ")
    private String templateValue;
    @Column(columnDefinition = "longtext ")
    private String templateContent;
    private String path;
//    @Column(name = "template_order",columnDefinition = "int default 0")
//    private Integer order;

    private Boolean isSystem;
    @Column(columnDefinition = "longtext ")
    private String resource;
    @Column(columnDefinition = "longtext ")
    private String base;
    @Column(columnDefinition = "bit(1) default false")
    private Boolean tree=false;
    @Column(name = "parse_")
    private Boolean parse=true;

    @Column(name = "original_content", columnDefinition = "longtext")
    private String originalContent;

    private Integer articleSize=0;

  }
