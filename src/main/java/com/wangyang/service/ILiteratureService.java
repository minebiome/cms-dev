package com.wangyang.service;

import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.Literature;
import com.wangyang.pojo.entity.Task;
import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.pojo.vo.ArticleVO;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.pojo.vo.CategoryVO;
import com.wangyang.pojo.vo.ContentVO;
import com.wangyang.service.base.IContentService;
import com.wangyang.service.base.ICrudService;

import java.util.List;
import java.util.Set;

public interface ILiteratureService  extends IContentService<Literature,Literature, ContentVO> {
    List<Literature> listByKeys(Set<String> literatureStrIds);

    List<Literature> listByCollectionId(Integer collectionId);

    void generateHtml(int userId);
}
