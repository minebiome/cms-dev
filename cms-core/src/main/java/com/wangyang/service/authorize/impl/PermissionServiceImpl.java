package com.wangyang.service.authorize.impl;


import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.pojo.annotation.CommentRole;
import com.wangyang.pojo.annotation.WxRole;
import com.wangyang.pojo.authorize.*;
import com.wangyang.pojo.support.RoleUrl;
import com.wangyang.service.authorize.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements IPermissionService {

    @Value("${cms.authorizeInit}")
    private Boolean authorizeInit;
    @Autowired
    IResourceService resourceService;
    @Autowired
    IRoleResourceService roleResourceService;
    @Autowired
    IRoleService roleService;
    @Autowired
    IAPIUserService apiUserService;
    @Autowired
    IUserRoleService userRoleService;
    @Autowired
    IUserService userService;
    @Autowired
    private ApplicationContext applicationContext;

    private final Pattern pattern = Pattern.compile("\\{.*?\\}");

    /**
     * 根据uri查找所需要的角色
     *
     * @param uri
     * @return
     */
    @Override
    public Set<Role> findRolesByResource(String uri) {
        /**
         * 查数据库的进行缓存
         */
        List<Resource> resources = resourceService.listAll();
//        Map<String, Resource> resourceMap = resources.stream()
//                .collect(Collectors.toMap(resource ->
//                        resource.getMethod()+resource.getUrl(),resource -> resource));
        List<Role> roles = roleService.listAll();
        List<RoleResource> roleResources = roleResourceService.listAll();


        Resource findResource = null;
        for (Resource resource : resources) {
            String url = resource.getMethod() + resource.getUrl();
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                String regUrl = matcher.replaceAll("(.*?)");
                boolean matches = uri.matches(regUrl);
                if (matches) {
                    findResource = resource;
                    break;
                }
            } else if (url.equals(uri)) {
                findResource = resource;
                break;
            }
        }
        Set<Role> needRoles = new HashSet<>();
        if (findResource == null) {
            needRoles.add(new Role("anonymous"));
            return needRoles;
        }

        Resource finalFindResource = findResource;
        roleResources = roleResources.stream()
                .filter(roleResource -> roleResource.getResourceId().equals(finalFindResource.getId()))
                .collect(Collectors.toList());
        Set<Integer> roleIds = ServiceUtil.fetchProperty(roleResources, RoleResource::getRoleId);
        needRoles = roles.stream()
                .filter(role -> roleIds.contains(role.getId()))
                .collect(Collectors.toSet());

        if (needRoles.size() == 0) {
            needRoles.add(new Role("anonymous"));
        }
        return needRoles;
    }


    @Override
    public ApiUserDetailDTO findSDKRolesByResource(String authorize) {
        APIUser apiUser = apiUserService.findByAuthorize(authorize);
        List<Role> roles = roleService.listAll();
        List<UserRole> userRoles = userRoleService.listAll();
        ApiUserDetailDTO apiUserDetailDTO = new ApiUserDetailDTO();
        Set<Role> needRoles;
        if (apiUser == null) {
            needRoles = new HashSet<>();
            needRoles.add(new Role("anonymous"));
            apiUserDetailDTO.setRoles(needRoles);
            return apiUserDetailDTO;
        }
        userRoles = userRoles.stream()
                .filter(userRole -> userRole.getUserId().equals(apiUser.getId()))
                .collect(Collectors.toList());
        Set<Integer> roleIds = ServiceUtil.fetchProperty(userRoles, UserRole::getRoleId);
        needRoles = roles.stream()
                .filter(role -> roleIds.contains(role.getId()))
                .collect(Collectors.toSet());
        BeanUtils.copyProperties(apiUser, apiUserDetailDTO);
        apiUserDetailDTO.setRoles(needRoles);
        return apiUserDetailDTO;
    }

//    public void initCommentUser(){
//
//
//        List<RoleResource> roleResources = new ArrayList<>();
//        RoleResource roleResource = new RoleResource();
//        Resource resource = new Resource();
//        roleResource.setRoleId(role.getId());
//        roleResource.setResourceId(resource.getId());
//        roleResources.add(roleResource);
//        roleResourceService.saveAll(roleResources);
//    }

    @Override
    public void init() {

        Role role = roleService.findByEnName("ADMIN");
        if (role == null) {
            role = new Role();
            role.setName("ADMIN");
            role.setEnName("ADMIN");
            role = roleService.save(role);
        }
        User user = userService.findUserByUsername("admin");

        if (user == null) {
            user = new User();
            user.setUsername("admin");
            user.setGender(0);
            user.setPassword("123456");
            user=userService.addUser(user);
            UserRole userRole = new UserRole(user.getId(), role.getId());
            userRoleService.save(userRole);
        }


        Role commentRole = roleService.findByEnName("COMMENT");
        if (commentRole == null) {
            commentRole = new Role();
            commentRole.setName("COMMENT");
            commentRole.setEnName("COMMENT");
            commentRole = roleService.save(commentRole);
        }

        Role wxRole = roleService.findByEnName(CMSUtils.getWxRole());
        if (wxRole == null) {
            wxRole = new Role();
            wxRole.setName(CMSUtils.getWxRole());
            wxRole.setEnName(CMSUtils.getWxRole());
            wxRole = roleService.save(commentRole);
        }
//        User commentUser = userService.findUserByUsername("test");
//        if (commentUser == null) {
//            commentUser = new User();
//            commentUser.setUsername("test");
//            commentUser.setGender(0);
//            commentUser.setPassword("123456");
//            commentUser=userService.addUser(commentUser);
//
//            UserRole userRole = new UserRole(commentUser.getId(), commentRole.getId());
//            userRoleService.save(userRole);
//        }
//        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(RequestMapping.class);
        RequestMappingHandlerMapping mapping = applicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> methodMap = mapping.getHandlerMethods();
        List<Resource> resources = new ArrayList<>();
        List<RoleUrl> roleResourceName = new ArrayList<>();
        for (RequestMappingInfo info : methodMap.keySet()) {
            Resource resource = new Resource();
            HandlerMethod handlerMethod = methodMap.get(info);
            Set<String> urlSet = info.getPatternsCondition().getPatterns();
            String url = urlSet.iterator().next();
            if (url.equals("/error")) continue;
            Set<RequestMethod> methodSet = info.getMethodsCondition().getMethods();
            String methodName;
            if (methodSet.size() != 0) {
                methodName = methodSet.iterator().next().name();
            } else {
                methodName = "";
            }


            resource.setMethod(methodName);
            resource.setUrl(url);
            Method method = handlerMethod.getMethod();
            if (!method.isAnnotationPresent(Anonymous.class)) {
//                Anonymous authorize = method.getAnnotation(Anonymous.class);
//                String roleName = authorize.role();
                RoleUrl roleUrl = new RoleUrl(role.getId(), url, methodName);
                roleResourceName.add(roleUrl);
            }

            if(method.isAnnotationPresent(CommentRole.class)){
                RoleUrl roleUrl = new RoleUrl(commentRole.getId(), url, methodName);
                roleResourceName.add(roleUrl);
            }
            if(method.isAnnotationPresent(WxRole.class)){
                RoleUrl roleUrl = new RoleUrl(wxRole.getId(), url, methodName);
                roleResourceName.add(roleUrl);
            }

            resources.add(resource);
        }
        List<Resource> dataBaseResource = resourceService.listAll();
        Map<String, Resource> systemResourceMap = resources.stream()
                .collect(Collectors.toMap(resource -> resource.getMethod() + resource.getUrl(), resource -> resource));
        Map<String, Resource> dataBaseResourceMap = dataBaseResource.stream()
                .collect(Collectors.toMap(resource -> resource.getMethod() + resource.getUrl(), resource -> resource));

        MapDifference<String, Resource> difference = Maps.difference(systemResourceMap, dataBaseResourceMap);
        Map<String, Resource> onlyOnLeft = difference.entriesOnlyOnLeft();
        List<Resource> addResource = onlyOnLeft.values().stream().collect(Collectors.toList());
        resourceService.saveAll(addResource);
        Map<String, Resource> onlyOnRight = difference.entriesOnlyOnRight();
        List<Resource> removeResource = onlyOnRight.values().stream().collect(Collectors.toList());
        resourceService.deleteAll(removeResource);

        if (authorizeInit) {
            List<Role> roles = roleService.listAll();
            Map<Integer, Role> roleMap = ServiceUtil.convertToMap(roles, Role::getId);
            List<Resource> resourceResult = resourceService.listAll();
            Map<String, Resource> resourceMap = resourceResult.stream()
                    .collect(Collectors.toMap(resource -> resource.getMethod() + resource.getUrl(), resource -> resource));

            List<RoleResource> roleResources = roleResourceName.stream().map(
                    roleUrl -> {
                        RoleResource roleResource = new RoleResource();
                        Integer roleId = roleUrl.getRoleId();
                        Resource resource = resourceMap.get(roleUrl.getMethod() + roleUrl.getUrl());
                        roleResource.setResourceId(resource.getId());
                        roleResource.setRoleId(roleId);
                        return roleResource;
                    }).collect(Collectors.toList());
            roleResourceService.deleteAll();
            roleResourceService.saveAll(roleResources);
//            initCommentUser(resourceMap);
        }
    }
}