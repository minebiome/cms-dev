package com.wangyang.repository.base;

import com.wangyang.common.repository.BaseRepository;
import com.wangyang.pojo.entity.base.Content;

//@NoRepositoryBean
public interface ContentRepository<T extends Content>
        extends BaseRepository<T, Integer> {
}
