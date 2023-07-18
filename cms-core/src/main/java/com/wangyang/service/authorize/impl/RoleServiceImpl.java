package com.wangyang.service.authorize.impl;


import com.wangyang.common.exception.UserException;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.pojo.authorize.*;
import com.wangyang.pojo.dto.RoleDto;
import com.wangyang.common.enums.CrudType;
import com.wangyang.common.pojo.BaseVo;
import com.wangyang.repository.authorize.RoleRepository;
import com.wangyang.service.authorize.IRoleResourceService;
import com.wangyang.service.authorize.IRoleService;
import com.wangyang.service.authorize.IUserRoleService;
import com.wangyang.common.service.AbstractCrudService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/5/5
 */
@Service
//@Transactional
@Slf4j
public class RoleServiceImpl extends AbstractCrudService<Role,Role, BaseVo,Integer>
            implements IRoleService {


    private final RoleRepository roleRepository;
    private final IUserRoleService userRoleService;
    private final IRoleResourceService roleResourceService;

    public RoleServiceImpl(RoleRepository roleRepository,
                           IUserRoleService userRoleService,
                           IRoleResourceService roleResourceService) {
        super(roleRepository);
        this.roleRepository=roleRepository;
        this.userRoleService=userRoleService;
        this.roleResourceService=roleResourceService;
    }


    @Override
    public List<Role> listAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> findRolesByUserId(Integer userId) {
        List<UserRole> userRoles = userRoleService.findByUserId(userId);
        if (CollectionUtils.isEmpty(userRoles)) {
            return new ArrayList<>();
        }
        List<Integer> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        Set<Role> roles = roleRepository.findByIdIn(roleIds);
        return new ArrayList<>(roles);
    }

    @Override
    public Role addRole(Role role) {
        return super.save(role);
    }

    @Override
    public Role findRoleById(int id) {
        List<Role> roleList = listAll().stream()
                .filter(role -> role.getId() == id)
                .collect(Collectors.toList());
        return roleList.size()==0?null:roleList.get(0);
    }

    @Override
    public Role delRole(int id) {
        Role role = findById(id);
        if(role.getEnName().equals("ADMIN")){
            throw new UserException("ADMIN角色不能删除！");
        }
        List<RoleResource> roleResources = roleResourceService.findByRoleId(role.getId());
        roleResourceService.deleteAll(roleResources);
        roleRepository.delete(role);
        return role;
    }

    @Override
    public Page<RoleDto> pageRole(Pageable pageable) {
        return roleRepository.findAll(pageable).map(role -> {
            RoleDto roleDto = new RoleDto();
            BeanUtils.copyProperties(role,roleDto);
            return roleDto;
        });
    }

    @Override
    public Role addRole(RoleParam roleParam) {
        Role role = new Role();
        BeanUtils.copyProperties(roleParam,role);
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Integer id, RoleParam roleParam) {
        Role role = findById(id);
        BeanUtils.copyProperties(roleParam,role);
        return roleRepository.save(role);
    }

    @Override
    public Role findByEnName(String name){


        List<Role> roleList = listAll().stream()
                .filter(role -> role.getEnName().equals(name))
                .collect(Collectors.toList());
        return roleList.size()==0?null:roleList.get(0);
    }


    public List<Role> findByIds(Iterable<Integer> inputIds){
        Set<Integer> ids = (Set<Integer> )inputIds;
        List<Role> roleList = listAll().stream()
                .filter(role -> ids.contains(role.getId()))
                .collect(Collectors.toList());
        return roleList;
    }
    public List<Role> findByWithoutIds(Iterable<Integer> inputIds){
        Set<Integer> ids = (Set<Integer> )inputIds;
        List<Role> roleList = listAll().stream()
                .filter(role -> !ids.contains(role.getId()))
                .collect(Collectors.toList());
        return roleList;
    }

    @Override
    public List<RoleVO> findByUserId(Integer id) {
        List<UserRole> userRoles = userRoleService.findByUserId(id);
        Set<Integer> roleIds = ServiceUtil.fetchProperty(userRoles, UserRole::getRoleId);
        List<Role> roles = findByIds(roleIds);
        Map<Integer, Role> roleMap = ServiceUtil.convertToMap(roles, Role::getId);
        List<RoleVO> resourceVOS = userRoles.stream().map(userRole -> {
            RoleVO roleVO = new RoleVO();
            Role role = roleMap.get(userRole.getRoleId());
            BeanUtils.copyProperties(role,roleVO);
            roleVO.setUserRoleId(userRole.getId());
            roleVO.setUserId( userRole.getUserId());

            return roleVO;
        }).collect(Collectors.toList());
        return resourceVOS;
    }

    @Override
    public List<Role> findByUser(Integer id) {
        List<UserRole> userRoles = userRoleService.findByUserId(id);
        Set<Integer> roleIds = ServiceUtil.fetchProperty(userRoles, UserRole::getRoleId);
        List<Role> roles = findByIds(roleIds);
        return roles;
    }

    @Override
    public List<Role> findByWithoutUserId(Integer id) {
        List<UserRole> userRoles = userRoleService.findByUserId(id);
        Set<Integer> roleIds = ServiceUtil.fetchProperty(userRoles, UserRole::getRoleId);
        List<Role> roles = findByWithoutIds(roleIds);
        return roles;
    }




    @Override
    public List<RoleVO> findByRoleId(Integer id) {
        List<RoleResource> roleResources = roleResourceService.findByResourceId(id);
        Set<Integer> roleIds = ServiceUtil.fetchProperty(roleResources, RoleResource::getRoleId);
        List<Role> roles = findByIds(roleIds);
        Map<Integer, Role> resourceMap = ServiceUtil.convertToMap(roles, Role::getId);

        List<RoleVO> resourceVOS = roleResources.stream().map(roleResource -> {
            RoleVO roleVO = new RoleVO();
            Role role = resourceMap.get(roleResource.getRoleId());
            BeanUtils.copyProperties(role,roleVO);
            roleVO.setResourceRoleId(roleResource.getId());
            roleVO.setResourceId( roleResource.getResourceId());

            return roleVO;
        }).collect(Collectors.toList());
        return resourceVOS;
    }

    @Override
    public List<Role> findByWithoutRoleId(Integer id) {
        List<RoleResource> roleResources = roleResourceService.findByRoleId(id);
        Set<Integer> roleIds = ServiceUtil.fetchProperty(roleResources, RoleResource::getResourceId);
        List<Role> roles = findByWithoutIds(roleIds);
        return roles;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
