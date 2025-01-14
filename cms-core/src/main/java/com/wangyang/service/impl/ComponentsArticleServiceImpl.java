package com.wangyang.service.impl;

import com.wangyang.common.CmsConst;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.pojo.authorize.BaseAuthorize;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Components;
import com.wangyang.pojo.entity.ComponentsArticle;
import com.wangyang.pojo.entity.base.Content;
import com.wangyang.pojo.vo.ContentVO;
import com.wangyang.repository.ComponentsArticleRepository;
import com.wangyang.repository.ComponentsRepository;
import com.wangyang.service.IArticleService;
import com.wangyang.service.IComponentsArticleService;
import com.wangyang.service.IComponentsService;
import com.wangyang.service.base.IAuthorizeService;
import com.wangyang.service.base.IContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Service
public class ComponentsArticleServiceImpl implements IComponentsArticleService {



//    @Autowired
//    IComponentsService componentsService;

    @Autowired
    ComponentsRepository componentsRepository;
    @Autowired
    ComponentsArticleRepository componentsArticleRepository;


    @Autowired
    @Qualifier("contentServiceImpl")
    IContentService<Content,Content, ContentVO> contentService;


//    @Override
//    public List<ComponentsArticle> findByComponentsId(Integer id){
//        List<ComponentsArticle> componentsArticleList = componentsArticleRepository.findAll(new Specification<ComponentsArticle>() {
//            @Override
//            public Predicate toPredicate(Root<ComponentsArticle> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                return criteriaQuery.where(criteriaBuilder.equal(root.get("componentId"), id)).getRestriction();
//            }
//        }, Sort.by(Sort.Direction.DESC, "order"));
//        return componentsArticleList;
//    }
    @Override
    public List<ComponentsArticle> findByComponentId(Integer componentId){
        return componentsArticleRepository.findByComponentId(componentId);
    }
    @Override
    public List<ComponentsArticle> findByArticleId(Integer articleId){
        return componentsArticleRepository.findByArticleId(articleId);
    }
    public ComponentsArticle add(int articleId, int componentsId){
        Content content = contentService.findById(articleId);
        Components components = componentsRepository.findById(componentsId).get();
        ComponentsArticle findComponentsArticle = componentsArticleRepository.findByArticleIdAndComponentId(content.getId(), componentsId);
        if(findComponentsArticle!=null){
            throw new ObjectException(content.getTitle()+"已经在组件"+components.getName()+"中！！！");
        }
        ComponentsArticle componentsArticle = new ComponentsArticle();
        componentsArticle.setArticleId(content.getId());
        componentsArticle.setComponentId(components.getId());
        return  componentsArticleRepository.save(componentsArticle);
    }

    @Override
    public ComponentsArticle add(String viewName, int componentsId){
        Content content = contentService.findByViewName(viewName);
        Components components = componentsRepository.findById(componentsId).get();

        if(content==null){
            throw new ObjectException("要添加的内容不存在！！");
        }
        if(components==null){
            throw new ObjectException("要添加的组件不存在！！");
        }
        if(components.getDataName().equals(CmsConst.ARTICLE_DATA)){
            ComponentsArticle findComponentsArticle = componentsArticleRepository.findByArticleIdAndComponentId(content.getId(), componentsId);
            if(findComponentsArticle!=null){
                throw new ObjectException("["+content.getTitle()+"]已经在组件["+components.getName()+"]中！！！");
            }
            ComponentsArticle componentsArticle = new ComponentsArticle();
            componentsArticle.setArticleId(content.getId());
            componentsArticle.setComponentId(components.getId());
            return  componentsArticleRepository.save(componentsArticle);
        }
        throw new ObjectException("文章["+content.getTitle()+"]不能添加到组件["+components.getName()+"]中");
    }

    @Override
    public void delete(int id){
        contentService.delBy(id);
    }

    @Override
    public ComponentsArticle delete(int articleId, int componentsId){
        ComponentsArticle findComponentsArticle = componentsArticleRepository.findByArticleIdAndComponentId(articleId, componentsId);
        if(findComponentsArticle!=null){
            componentsArticleRepository.deleteById(findComponentsArticle.getId());

        }
        return findComponentsArticle;
    }






}
