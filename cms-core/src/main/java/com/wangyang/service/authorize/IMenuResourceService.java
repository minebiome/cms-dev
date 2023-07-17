package com.wangyang.service.authorize;

import com.wangyang.pojo.authorize.MenuResource;
import com.wangyang.pojo.authorize.MenuResourceDTO;
import com.wangyang.pojo.authorize.Role;

import java.util.List;
import java.util.Set;

public interface IMenuResourceService {

    /**
     * 添加菜单
     * @param resource  菜单信息
     * @return
     */
    MenuResource add(MenuResource resource);

    Boolean setRoleMenus(Integer roleId, List<Integer> menuIds);

    List<MenuResourceDTO> getMenuTree(Set<Role> roles);

}
