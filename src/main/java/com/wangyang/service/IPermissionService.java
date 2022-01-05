package com.wangyang.service;


import com.wangyang.pojo.authorize.ApiUserDetailDTO;
import com.wangyang.pojo.authorize.Role;

import java.util.Set;

public interface IPermissionService {
    Set<Role> findRolesByResource(String uri);

    ApiUserDetailDTO findSDKRolesByResource(String authorize);

    void init();
}
