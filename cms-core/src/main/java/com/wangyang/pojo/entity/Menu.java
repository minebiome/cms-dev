package com.wangyang.pojo.entity;

import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

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
