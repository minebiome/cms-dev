package com.wangyang.service;

import com.wangyang.pojo.dto.CategoryArticleListDao;
import com.wangyang.pojo.dto.CategoryContentListDao;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Template;
import com.wangyang.pojo.entity.base.Content;
import com.wangyang.pojo.vo.ArticleVO;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.pojo.vo.CategoryVO;
import com.wangyang.pojo.vo.ContentVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

public interface IContentServiceEntity extends com.wangyang.service.base.IContentService<Content,Content, ContentVO> {
    Page<Content> pageArticleByCategoryIds(Set<Integer> ids, Boolean isDesc, PageRequest pageRequest);
    List<Content> listArticleByCategoryIds(Set<Integer> ids, Boolean isDesc);

    Page<ContentVO> convertToPageVo(Page<Content> contentPage);

    void addParentCategory(List<CategoryVO> categoryVOS, Integer parentId);

    CategoryContentListDao findCategoryContentBy(CategoryVO category, Template template, int page);

    List<ContentVO> listVoTree(Set<Integer> ids, Boolean isDesc);
}
