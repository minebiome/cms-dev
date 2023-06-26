package com.wangyang.repository;

import com.wangyang.pojo.entity.Components;
import com.wangyang.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ComponentsRepository extends BaseRepository<Components,Integer> {

    Components findByViewName(String viewName);
    Components findByEnName(String enName);
}
