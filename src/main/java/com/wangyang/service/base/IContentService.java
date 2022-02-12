package com.wangyang.service.base;

import com.wangyang.pojo.entity.base.Content;

public interface IContentService<ARTICLE extends Content>  extends ICrudService<ARTICLE,Integer>{
    ARTICLE createOrUpdate(ARTICLE article);

//    ARTICLE previewSave(ARTICLE article);
}
