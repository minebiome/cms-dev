package com.wangyang.pojo.support;

import lombok.Data;

@Data
public class RoleUrl {
    private Integer roleId;
    private String url;
    private String method;

    public RoleUrl(Integer roleId, String url, String method) {
        this.roleId = roleId;
        this.url = url;
        this.method = method;
    }
}
