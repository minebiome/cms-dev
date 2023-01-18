package com.wangyang.repository;

import com.wangyang.pojo.entity.Components;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ComponentsRepository extends JpaRepository<Components,Integer>
        , JpaSpecificationExecutor<Components> {

    Components findByViewName(String viewName);
    Components findByEnName(String enName);
}
