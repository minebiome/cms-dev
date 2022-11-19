package com.wangyang.service.base;

import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.base.BaseCategory;
import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.pojo.entity.base.Content;
import com.wangyang.pojo.enums.CrudType;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.repository.base.BaseCategoryRepository;
import com.wangyang.repository.base.BaseRepository;

public abstract class AbstractBaseCategoryServiceImpl <CATEGORY extends BaseCategory,CATEGORYDTO extends BaseEntity,CATEGORYVO extends BaseVo>  extends AbstractCrudService<CATEGORY,CATEGORYDTO,CATEGORYVO,Integer>
        implements IBaseCategoryService<CATEGORY,CATEGORYDTO,CATEGORYVO>{

    BaseCategoryRepository<CATEGORY> baseCategoryRepository;
    public AbstractBaseCategoryServiceImpl(BaseCategoryRepository<CATEGORY> baseCategoryRepository) {
        super(baseCategoryRepository);
        this.baseCategoryRepository=baseCategoryRepository;
    }
}
