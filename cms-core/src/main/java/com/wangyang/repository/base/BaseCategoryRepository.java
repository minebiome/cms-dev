package com.wangyang.repository.base;

import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.base.BaseCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseCategoryRepository<T extends BaseCategory>  extends BaseRepository<T,Integer> {
}
