package com.wangyang.repository.base;

import com.wangyang.pojo.authorize.BaseAuthorize;
import com.wangyang.pojo.entity.base.Content;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ContentRepository<T extends Content>
        extends BaseRepository<T, Integer> {
}
