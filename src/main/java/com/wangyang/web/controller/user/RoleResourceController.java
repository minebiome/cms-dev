package com.wangyang.web.controller.user;

import com.wangyang.pojo.authorize.RoleResource;
import com.wangyang.service.IRoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/role_resource")
public class RoleResourceController {
    @Autowired
    IRoleResourceService roleResourceService;

    @PostMapping
    public RoleResource save(@RequestBody RoleResource resource){
        return roleResourceService.save(resource);
    }

    @GetMapping("/del/{id}")
    public RoleResource del(@PathVariable("id") Integer id){
        RoleResource roleResource = roleResourceService.delBy(id);
        return roleResource;
    }
}
