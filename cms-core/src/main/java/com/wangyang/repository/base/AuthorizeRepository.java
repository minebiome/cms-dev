package com.wangyang.repository.base;

import com.wangyang.pojo.authorize.BaseAuthorize;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;


public interface AuthorizeRepository<T extends BaseAuthorize>
        extends BaseRepository<T, Integer> {
}