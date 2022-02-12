package com.wangyang.service.authorize;


import com.wangyang.pojo.authorize.UserRole;
import com.wangyang.service.base.ICrudService;

import java.util.List;

public interface IUserRoleService  extends ICrudService<UserRole, Integer> {

    UserRole findBy(Integer userId, Integer roleId);

    List<UserRole> findByUserId(Integer id);

    List<UserRole> findByRoleId(Integer roleId);
}
