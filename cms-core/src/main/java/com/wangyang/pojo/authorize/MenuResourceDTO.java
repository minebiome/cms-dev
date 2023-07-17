package com.wangyang.pojo.authorize;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResourceDTO {

    private Integer parentId;

    /*
    菜单name
     */
    private String name;

    /*
    菜单名称
     */
    private String title;

    private String component;

    private String icon;

    /*
    菜单url
     */
    private String url;

    private List<MenuResourceDTO> children;
}
