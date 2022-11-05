package com.wangyang.repository;

import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.Literature;
import com.wangyang.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LiteratureRepository extends BaseRepository<Literature,Integer>
        , JpaSpecificationExecutor<Literature> {
}
