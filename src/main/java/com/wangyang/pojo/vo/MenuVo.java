package com.wangyang.pojo.vo;

import com.wangyang.pojo.entity.Menu;
import lombok.Data;

@Data
public class MenuVo extends BaseVo<Menu>{
    private Boolean status;
    private Integer categoryId;
    private Integer sheetId;
    private String icon;
    private String name;
    private String target;
    private String urlName;
}
