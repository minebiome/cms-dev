package com.wangyang.repository;

import com.wangyang.pojo.entity.Template;
import com.wangyang.pojo.enums.TemplateType;
import com.wangyang.common.repository.BaseRepository;

import java.util.List;

public interface TemplateRepository  extends BaseRepository<Template,Integer> {
    List<Template> findByTemplateType(TemplateType type);

    Template findByEnName(String enName);

}
