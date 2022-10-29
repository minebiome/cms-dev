package com.wangyang.pojo.entity;

import com.wangyang.pojo.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Menu extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    private int parentId;
    private Boolean status=true;
    private Integer categoryId;
    private Integer sheetId;
    private String icon;
    private String name;
    private String target;
    private String urlName;


}
