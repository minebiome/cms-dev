package com.wangyang.repository;

import com.wangyang.pojo.entity.Template;
import com.wangyang.pojo.enums.TemplateType;
import com.wangyang.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TemplateRepository  extends BaseRepository<Template,Integer> {
    List<Template> findByTemplateType(TemplateType type);

    Template findByEnName(String enName);

}
