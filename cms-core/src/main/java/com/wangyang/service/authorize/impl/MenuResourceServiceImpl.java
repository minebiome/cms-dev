package com.wangyang.service.authorize.impl;

import com.wangyang.pojo.authorize.MenuResource;
import com.wangyang.pojo.authorize.MenuResourceDTO;
import com.wangyang.pojo.authorize.Role;
import com.wangyang.pojo.authorize.RoleMenuResource;
import com.wangyang.repository.authorize.MenuResourceRepository;
import com.wangyang.repository.authorize.RoleMenuResourceRepository;
import com.wangyang.repository.authorize.RoleRepository;
import com.wangyang.service.authorize.IMenuResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class MenuResourceServiceImpl implements IMenuResourceService {

    @Autowired
    private MenuResourceRepository menuResourceRepository;

    @Autowired
    private RoleMenuResourceRepository roleMenuResourceRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = false)
    @Override
    public MenuResource add(MenuResource resource) {
        Date currentTime = new Date();
        resource.setCreateDate(currentTime);
        resource.setUpdateDate(currentTime);
        return menuResourceRepository.save(resource);
    }

    @Transactional(readOnly = false)
    @Override
    public Boolean setRoleMenus(Integer roleId, List<Integer> menuIds) {
        List<RoleMenuResource> roleMenuResources = new ArrayList<>();
        Date currentTime = new Date();
        for (Integer menuId : menuIds) {
            RoleMenuResource build = RoleMenuResource.builder()
                    .roleId(roleId)
                    .menuResourceId(menuId)
                    .build();
            build.setCreateDate(currentTime);
            build.setUpdateDate(currentTime);
            roleMenuResources.add(build);
        }
        roleMenuResourceRepository.saveAll(roleMenuResources);
        return true;
    }

    @Override
    public List<MenuResourceDTO> getMenuTree(Set<Role> roles) {
        roles = roleRepository
                .findByEnNameIn(roles.stream().map(Role::getEnName).collect(Collectors.toList()));
        List<MenuResource> menuResources = menuResourceRepository.findAll();
        Boolean admin = false;
        for (Role v : roles) {
            if ("ADMIN".equalsIgnoreCase(v.getEnName())) {
                admin = true;
                break;
            }
        }
        if (!admin) {
            List<RoleMenuResource> roleMenuResources = roleMenuResourceRepository
                    .findByRoleIdIn(roles.stream().map(Role::getId).collect(Collectors.toList()));
            Set<Integer> menuResourceIds = roleMenuResources.stream()
                    .map(RoleMenuResource::getMenuResourceId).collect(Collectors.toSet());
            menuResources = menuResources.stream().filter(v -> menuResourceIds.contains(v.getId())).collect(Collectors.toList());
        }
        Map<Integer, List<MenuResource>> menuResourceMap = menuResources.stream().collect(Collectors.groupingBy(MenuResource::getParentId));

        return parseMenuResourceTree(menuResourceMap.get(0), menuResourceMap);
    }

    private List<MenuResourceDTO> parseMenuResourceTree(List<MenuResource> menuResources
            , Map<Integer, List<MenuResource>> menuResourceMap) {
        List<MenuResourceDTO> menuResourceDtos = new ArrayList<>();
        if (CollectionUtils.isEmpty(menuResources)) {
            return menuResourceDtos;
        }
        menuResources.forEach(v -> {
            menuResourceDtos.add(MenuResourceDTO.builder()
                            .name(v.getName())
                            .url(v.getUrl())
                            .title(v.getTitle())
                            .component(v.getComponent())
                            .icon(v.getIcon())
                            .parentId(v.getParentId())
                            .children(parseMenuResourceTree(menuResourceMap.get(v.getId()), menuResourceMap))
                    .build());
        });
        return menuResourceDtos;
    }
}
