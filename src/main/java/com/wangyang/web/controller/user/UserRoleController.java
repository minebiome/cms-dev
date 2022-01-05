package com.wangyang.web.controller.user;


import com.wangyang.pojo.authorize.UserRole;
import com.wangyang.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user_role")
public class UserRoleController {
    @Autowired
    IUserRoleService userRoleService;

    @PostMapping
    public UserRole save(@RequestBody UserRole userRole){
        return userRoleService.save(userRole);
    }


    @GetMapping("/del/{id}")
    public UserRole del(@PathVariable("id") Integer id){
        UserRole userRole = userRoleService.delBy(id);
        return userRole;
    }
}
