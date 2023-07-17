package com.wangyang.repository.authorize;

import com.wangyang.pojo.authorize.RoleMenuResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleMenuResourceRepository extends JpaRepository<RoleMenuResource, Integer> {

    List<RoleMenuResource> findByRoleId(Integer roleId);

    List<RoleMenuResource> findByRoleIdIn(List<Integer> roleIds);
}
