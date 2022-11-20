package com.wangyang.pojo.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryParam {

    private String description;
    @NotBlank(message = "Category name can't empty!!")
    private String name;
//    @NotBlank(message = "parentId  can't empty!!")
    private Integer parentId;
    private String templateName;
    private String viewName;
    private Boolean haveHtml;
    private String picPath;
    private String path;
    private Integer order;
    private Boolean recommend=false;
    private String articleTemplateName;
    private Integer articleListSize=10;
    private Boolean isDesc=true;
    private String icon;
}
