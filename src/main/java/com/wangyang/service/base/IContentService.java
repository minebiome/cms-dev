package com.wangyang.service.base;

import com.wangyang.pojo.entity.base.Content;
import com.wangyang.pojo.vo.BaseVo;

public interface IContentService<ARTICLE extends Content,ARTICLEDTO,ARTICLEVO>  extends ICrudService<ARTICLE,ARTICLEDTO,ARTICLEVO,Integer>{
    ARTICLE createOrUpdate(ARTICLE article);

//    ARTICLE previewSave(ARTICLE article);
}
