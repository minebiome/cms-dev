package com.wangyang.repository;

import com.wangyang.pojo.entity.Components;
import com.wangyang.common.repository.BaseRepository;

public interface ComponentsRepository extends BaseRepository<Components,Integer> {

    Components findByViewName(String viewName);
    Components findByEnName(String enName);
}
