package com.wangyang.pojo.authorize;

import lombok.Data;

@Data
public class ResourceVO extends Resource{
    private int resourceRoleId;
    private int roleId;
}
