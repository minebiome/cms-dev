package com.wangyang.web.controller.user;

import com.wangyang.pojo.authorize.Resource;
import com.wangyang.pojo.authorize.ResourceVO;
import com.wangyang.service.authorize.IResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    @Autowired
    IResourceService resourceService;
    @GetMapping
    public Page<Resource> page(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        return resourceService.pageBy(pageable);
    }
    @GetMapping("/listAll")
    public List<Resource> listResource(){
        return resourceService.listAll();
    }
    @GetMapping("/findByRoleId/{id}")
    public List<ResourceVO> findByRoleId(@PathVariable("id") Integer id){
        return  resourceService.findByRoleId(id);
    }

    @GetMapping("/findByWithoutRoleId/{id}")
    public List<Resource> listResource(@PathVariable("id") Integer id){
        return resourceService.findByWithoutRoleId(id);
    }
}
