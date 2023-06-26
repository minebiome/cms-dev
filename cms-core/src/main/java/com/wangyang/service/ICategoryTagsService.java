package com.wangyang.service;

import com.wangyang.pojo.entity.CategoryTags;
import com.wangyang.pojo.entity.Comment;
import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.pojo.vo.CommentVo;
import com.wangyang.service.base.ICrudService;

import java.util.List;
import java.util.Set;

public interface ICategoryTagsService extends ICrudService<CategoryTags, BaseEntity, BaseVo,Integer> {
    List<CategoryTags> listByTagIds(Set<Integer> ids);
}
