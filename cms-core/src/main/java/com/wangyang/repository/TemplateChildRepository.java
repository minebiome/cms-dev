package com.wangyang.repository;

import com.wangyang.pojo.entity.ComponentsArticle;
import com.wangyang.pojo.entity.Task;
import com.wangyang.pojo.entity.TemplateChild;
import com.wangyang.repository.base.BaseRepository;

import java.util.List;

public interface TemplateChildRepository extends BaseRepository<TemplateChild,Integer> {
    TemplateChild findByTemplateIdAndTemplateChildId(int templateId, int templateChildId);
    List<TemplateChild> findByTemplateId(int templateId);
}
