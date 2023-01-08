package com.wangyang.pojo.vo;

import com.wangyang.pojo.dto.CategoryDto;
import lombok.Data;

import java.util.List;

@Data
public class CategoryVO extends BaseVo<CategoryVO> {

//    private Integer id;
//    private Integer parentId;
//
    private String name;
    private String icon;
    private String viewName;
    private String path;
    private String linkPath;
    private Integer order;
    private String resource;
    private Boolean haveHtml;
    private Boolean recommend;
    private Boolean existNav;
    private Integer articleNumber;
    private String templateName;
    private String articleTemplateName;
    private String picPath;
    private String picThumbPath;
    private String description;
    private Boolean isDesc;
    private Integer articleListSize;



}
