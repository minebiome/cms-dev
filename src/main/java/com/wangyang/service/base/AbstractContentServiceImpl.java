package com.wangyang.service.base;

import com.wangyang.common.utils.MarkdownUtils;
import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.pojo.entity.base.Content;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.repository.ArticleRepository;
import com.wangyang.repository.base.BaseRepository;
import com.wangyang.repository.base.ContentRepository;
import com.wangyang.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public abstract class AbstractContentServiceImpl<ARTICLE extends Content,ARTICLEDTO extends BaseEntity,ARTICLEVO extends BaseVo>  extends AbstractCrudService<ARTICLE,ARTICLEDTO,ARTICLEVO,Integer>
        implements IContentService<ARTICLE,ARTICLEDTO,ARTICLEVO> {

//    @Autowired
//    IOptionService optionService;

//    @Autowired
//    ArticleRepository articleRepository;
    private ContentRepository<ARTICLE> contentRepository;
    public AbstractContentServiceImpl(ContentRepository<ARTICLE> contentRepository) {
        super(contentRepository);
        this.contentRepository=contentRepository;
    }

    @Override
    public ARTICLE createOrUpdate(ARTICLE article) {

        MarkdownUtils.renderHtml(article);
        return article;
    }

//    @Override
//    public ARTICLE previewSave(ARTICLE article) {
//
//
//            String[] renderHtml = MarkdownUtils.renderHtml(article.getOriginalContent());
//
//            article.setFormatContent(renderHtml[1]);
//
//            article.setToc(renderHtml[0]);
//
//        return article;
//    }


}
