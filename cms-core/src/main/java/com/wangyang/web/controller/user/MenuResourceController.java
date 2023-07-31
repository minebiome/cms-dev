package com.wangyang.web.controller.user;

import com.wangyang.pojo.authorize.MenuResource;
import com.wangyang.pojo.authorize.MenuResourceDTO;
import com.wangyang.pojo.authorize.Role;
import com.wangyang.pojo.authorize.UserDetailDTO;
import com.wangyang.service.authorize.IMenuResourceService;
import com.wangyang.util.AuthorizationUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/menuResource")
public class MenuResourceController {

    @Autowired
    private IMenuResourceService menuResourceService;

    @PostMapping
    public MenuResource add(@RequestBody MenuResource resource) {
        return menuResourceService.add(resource);
    }

    @PostMapping(value = "setRoleMenus/{roleId}")
    public Boolean setRoleMenus(@PathVariable("roleId") Integer roleId, @RequestBody List<Integer> menuIds) {
        return menuResourceService.setRoleMenus(roleId, menuIds);
    }

    @GetMapping("tree")
    public List<MenuResourceDTO> getMenuTree(HttpServletRequest request) {
        UserDetailDTO user = AuthorizationUtil.getUser(request);
        Set<Role> roles = user.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return new ArrayList<>();
        }
        return menuResourceService.getMenuTree(roles);
    }

}
