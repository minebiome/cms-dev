package com.wangyang.pojo.entity;

import com.wangyang.pojo.entity.base.BaseCategory;
import com.wangyang.pojo.entity.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;
@Entity
//@DiscriminatorValue(value = "0")
@Data
@Table(name = "t_category")
public class Category extends BaseCategory implements Serializable{

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    private String name;
//    @Column(columnDefinition = "int default 0")
//    private Integer parentId;
    private String description;
    @Column(columnDefinition = "int default 0")
    private Integer articleNumber;
//    private Integer templateId;
    private String templateName;
    @Column(columnDefinition = "bit(1) default true")
    private Boolean haveHtml=true;

    private String viewName;
//    @Column(columnDefinition = "bit(1) default true")
//    private Boolean status=true;
    private String picPath;
    private String path;
//    @Column(name = "category_order",columnDefinition = "int default 1")
//    private Integer order;
    @Column(columnDefinition = "bit(1) default false")
    private Boolean recommend=false;
    @Column(columnDefinition = "bit(1) default false")
    private Boolean existNav=false;
    private String articleTemplateName;
    private String icon;
    // 每页显示文章的数量
    private Integer articleListSize=10;
//    private Integer articleListPage=0;
    private Boolean isDesc=true;

    public Boolean getDesc() {
        return isDesc;
    }

    public void setDesc(Boolean desc) {
        isDesc = desc;
    }
}
