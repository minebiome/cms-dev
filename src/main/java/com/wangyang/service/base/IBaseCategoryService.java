package com.wangyang.service.base;

import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.base.BaseCategory;
import com.wangyang.pojo.entity.base.Content;

public interface IBaseCategoryService <CATEGORY extends BaseCategory,CATEGORYDTO,CATEGORYVO>  extends ICrudService<CATEGORY,CATEGORYDTO,CATEGORYVO,Integer>{
}
