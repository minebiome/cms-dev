package com.wangyang.pojo.authorize;

import lombok.Data;

@Data
public class RoleVO extends Role{
    private Integer resourceRoleId;
    private Integer userRoleId;
    private Integer resourceId;
    private Integer userId;
}
