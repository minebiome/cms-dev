package com.wangyang.repository.authorize;

import com.wangyang.pojo.authorize.BaseAuthorize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BaseAuthorizeRepository extends JpaRepository<BaseAuthorize, Integer>, JpaSpecificationExecutor<BaseAuthorize> {
}
