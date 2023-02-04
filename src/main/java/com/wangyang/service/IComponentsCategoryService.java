package com.wangyang.service;

import com.wangyang.pojo.entity.Comment;
import com.wangyang.pojo.entity.ComponentsArticle;
import com.wangyang.pojo.entity.ComponentsCategory;
import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.pojo.vo.CommentVo;
import com.wangyang.service.base.ICrudService;

import java.util.List;
import java.util.Set;

public interface IComponentsCategoryService extends ICrudService<ComponentsCategory, ComponentsCategory, BaseVo,Integer> {
    List<ComponentsCategory> findByCategoryId(Integer categoryId);

    ComponentsCategory add(String viewName, int componentsId);
    void delete(int id);

    ComponentsCategory delete(Integer categoryId, Integer componentId);

    List<ComponentsCategory> findByCategoryId(Set<Integer> categoryIds);
}
