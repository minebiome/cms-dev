package com.wangyang.repository.base;

import com.wangyang.common.repository.BaseRepository;
import com.wangyang.pojo.authorize.BaseAuthorize;


public interface AuthorizeRepository<T extends BaseAuthorize>
        extends BaseRepository<T, Integer> {
}
