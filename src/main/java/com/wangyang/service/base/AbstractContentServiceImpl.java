package com.wangyang.service.base;

import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.utils.MarkdownUtils;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.pojo.dto.CategoryContentListDao;
import com.wangyang.pojo.entity.ComponentsArticle;
import com.wangyang.pojo.entity.Template;
import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.pojo.entity.base.Content;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.pojo.vo.CategoryVO;
import com.wangyang.pojo.vo.ContentDetailVO;
import com.wangyang.pojo.vo.ContentVO;
import com.wangyang.repository.ArticleRepository;
import com.wangyang.repository.ComponentsArticleRepository;
import com.wangyang.repository.base.BaseRepository;
import com.wangyang.repository.base.ContentRepository;
import com.wangyang.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

//@Component
public abstract class AbstractContentServiceImpl<ARTICLE extends Content,ARTICLEDTO extends BaseEntity,ARTICLEVO extends BaseVo>  extends AbstractCrudService<ARTICLE,ARTICLEDTO,ARTICLEVO,Integer>
        implements IContentService<ARTICLE,ARTICLEDTO,ARTICLEVO> {

//    @Autowired
//    IOptionService optionService;
    @Autowired
    ComponentsArticleRepository componentsArticleRepository;
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


    @Override
    public Page<ARTICLE> pageContentByCategoryIds(Set<Integer> ids, Boolean isDesc, PageRequest pageRequest) {
        return null;
    }

    @Override
    public List<ARTICLE> listContentByCategoryIds(Set<Integer> ids, Boolean isDesc) {
        return null;
    }

    @Override
    public Page<ARTICLEVO> convertToPageVo(Page<ARTICLE> contentPage) {
        return null;
    }

    @Override
    public void addParentCategory(List<CategoryVO> categoryVOS, Integer parentId) {

    }

    @Override
    public CategoryContentListDao findCategoryContentBy(CategoryVO category, Template template, int page) {
        return null;
    }

    @Override
    public List<ARTICLEVO> listVoTree(Integer categoryId) {
        return null;
    }

    @Override
    public List<ARTICLEVO> listVoTree(Set<Integer> ids, Boolean isDesc) {
        return null;
    }

    @Override
    public void updateOrder(Integer id, List<ARTICLEVO> contentVOS) {

    }

    @Override
    public List<ARTICLEVO> listArticleVOBy(String viewName) {
        return null;
    }

    @Override
    public ContentDetailVO updateCategory(ARTICLE content, Integer baseCategoryId) {
        return null;
    }


    @Override
    public List<ARTICLEVO> listByComponentsId(int componentsId){
        List<ComponentsArticle> componentsArticles = componentsArticleRepository.findByComponentId(componentsId);
        Set<Integer> articleIds = ServiceUtil.fetchProperty(componentsArticles, ComponentsArticle::getArticleId);
//        List<Article> articles = articleRepository.findAllById(articleIds);
        List<ARTICLE> articles = contentRepository.findAll(new Specification<ARTICLE>() {
            @Override
            public Predicate toPredicate(Root<ARTICLE> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(root.get("id").in(articleIds)).getRestriction();
            }
        }, Sort.by(Sort.Direction.DESC,"articleInComponentOrder"));
        return convertToListVo(articles);
    }

    @Override
    public ARTICLE findByViewName(String viewName) {
        List<ARTICLE> contents = contentRepository.findAll(new Specification<ARTICLE>() {
            @Override
            public Predicate toPredicate(Root<ARTICLE> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("viewName"), viewName)).getRestriction();
            }
        });
        if(contents.size()==0){
            throw new ObjectException("查找的内容对象不存在");
        }
        return contents.get(0);
    }
}
