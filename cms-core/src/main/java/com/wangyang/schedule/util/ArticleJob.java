package com.wangyang.schedule.util;

import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.pojo.dto.ArticleDto;
import com.wangyang.pojo.dto.CategoryDto;
import com.wangyang.pojo.dto.TagsDto;
import com.wangyang.pojo.entity.*;
import com.wangyang.pojo.entity.Collection;
import com.wangyang.pojo.support.ScheduleOption;
import com.wangyang.pojo.support.TemplateOption;
import com.wangyang.pojo.support.TemplateOptionMethod;
import com.wangyang.pojo.vo.ArticleVO;
import com.wangyang.repository.ArticleTagsRepository;
import com.wangyang.repository.CategoryTagsRepository;
import com.wangyang.service.*;
import com.wangyang.service.impl.ArticleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.*;
import java.util.stream.Collectors;

//@Component
@Slf4j
@TemplateOption
@ScheduleOption
@Component("articleJob")

public class ArticleJob {

    @Autowired
    IArticleService articleService;

    @Autowired
    ITagsService tagsService;
    @Autowired
    ArticleTagsRepository articleTagsRepository;


    @Autowired
    ICategoryService categoryService;

    @Autowired
    IMenuService menuService;
    @Autowired
    IComponentsService componentsService;

    @Autowired
    ICollectionService collectionService;


    @Autowired
    IHtmlService htmlService;

    @Autowired
    ITemplateService templateService;




    public Object test1(String test){
        return "aaaa"+test;
    }

    //每天凌晨执行
    @ArticleJobAnnotation(jobName = "hotArticle",jobGroup = "ArticleJob",cornExpression = "0 0 0 * * ?")
    public void hotArticle(){
        Components components = componentsService.findByViewName("hotArticle");
        Object o = componentsService.getModel(components);
        TemplateUtil.convertHtmlAndSave(o, components);

    }

    @ArticleJobAnnotation(jobName = "likeArticle",jobGroup = "ArticleJob",cornExpression = "0 0 0 * * ?")
    public void likeArticle(){
        Components components = componentsService.findByViewName("likeArticle");
        Object o = componentsService.getModel(components);
        TemplateUtil.convertHtmlAndSave(o, components);
    }

    @ArticleJobAnnotation(jobName = "myArticle",jobGroup = "ArticleJob",cornExpression = "0 0 0 * * ?")
    public void myArticle(){
        Components components = componentsService.findByViewName("myArticle");
        Object o = componentsService.getModel(components);
        TemplateUtil.convertHtmlAndSave(o, components);
    }


//
//    //每天凌晨执行
//    @ArticleJobAnnotation(jobName = "newInformation",jobGroup = "ArticleJob",cornExpression = "0 0 0 * * ?")
//    public void newInformation(){
//        Optional<Tags> tags = tagsService.findBy(CmsConst.TAGS_INFORMATION);
//        if(tags.isPresent()){
//            log.info("Schedule 生成推荐文章,在"+tags.get().getName());
//            Page<ArticleDto> articleDtos = articleService.pageByTagId(tags.get().getId(), 5);
//            Map<String,Object> map = new HashMap<>();
//            map.put("view",articleDtos);
//            map.put("showUrl","/articleList?tagsId="+tags.get().getId());
//            map.put("name","最新资讯");
//            Template template = templateService.findByEnName(CmsConst.ARTICLE_LIST);
//            TemplateUtil.convertHtmlAndSave("html/components","newInformation",map,template);
//        }else {
//            log.info("Schedule 不能找到Tags"+CmsConst.TAGS_INFORMATION);
//        }
//    }

//    //每天凌晨执行
//    @ArticleJobAnnotation(jobName = "recommendArticle",jobGroup = "ArticleJob",cornExpression = "0 0 0 * * ?")
//    public void recommendArticle(){
//        Optional<Tags> tags = tagsService.findBy(CmsConst.TAGS_RECOMMEND);
//        if(tags.isPresent()){
//            log.info("Schedule 生成推荐文章,在"+tags.get().getName());
//            Page<ArticleDto> articleDtos = articleService.pageByTagId(tags.get().getId(), 5);
//            Map<String,Object> map = new HashMap<>();
//            map.put("view",articleDtos);
//            map.put("showUrl","/articleList?tagsId="+tags.get().getId());
//            map.put("name","推荐阅读");
//            Template template = templateService.findByEnName(CmsConst.ARTICLE_LIST);
//            TemplateUtil.convertHtmlAndSave("html/components","recommendArticle",map,template);
//        }else {
//            log.info("Schedule 不能找到Tags"+CmsConst.TAGS_RECOMMEND);
//        }
//    }

    @ArticleJobAnnotation(jobName = "tagsCategory",jobGroup = "ArticleJob",cornExpression = "0 0 0 * * ?")
    public void tagsCategory(){
        List<Category> categories = categoryService.listAll();
        htmlService.generateRecommendArticle(categories);
    }




    @ArticleJobAnnotation(jobName = "tagsTopList",jobGroup = "ArticleJob",cornExpression = "0 0 0 * * ?")
    public void tagsTopList(){

        Components components = componentsService.findByViewName("tagsList");
        Object o = componentsService.getModel(components);
        TemplateUtil.convertHtmlAndSave(o, components);
//
//        Template template = templateService.findByEnName(CmsConst.TAGS_LIST);
//        TemplateUtil.convertHtmlAndSave(CMSUtils.getTagPath(),"tagsList",model,template);
    }

//
//
    @ArticleJobAnnotation(jobName = "tagsArticleRecommend",jobGroup = "ArticleJob",cornExpression = "0 0 0 * * ?")
    public void tagsArticle(){
        List<Tags> tags = tagsService.listAll();
        for (Tags tag : tags){
            List<ArticleTags> articleTags = articleTagsRepository.findByTagsId(tag.getId());
            Set<Integer> articleIds = ServiceUtil.fetchProperty(articleTags, ArticleTags::getArticleId);
            Page<Article> articles = articleService.pageByIds(articleIds, 0, 5, null);
            List<Article> contents = articles.getContent();
            List<ArticleVO> articleVOS = articleService.convertToListVo(contents);
            Map<String,Object> map = new HashMap<>();
            map.put("articleVOS",articleVOS);
            if(tag.getEnName()==null){
                tag.setEnName(CMSUtils.randomViewName());
                tagsService.save(tag);
            }
            Template template = templateService.findByEnName(CmsConst.TAGS);
            TemplateUtil.convertHtmlAndSave(CMSUtils.getTagPath(),tag.getEnName(),map,template);
        }

    }

    /**
     * 生成每一个分类下热门文章用于嵌入文章页面
     */
    @ArticleJobAnnotation(jobName = "categoryArticleRecommend",jobGroup = "ArticleJob",cornExpression = "0 0 0 * * ?")
    public void categoryArticleRecommend(){
        List<Category> categories = categoryService.listAll();
        categories.forEach(category -> {
            PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("visits")));
//            Page<ArticleDto> articleDtos = articleService.pageDtoByCategory(category,pageRequest );
//            Page<Article> articles = articleRepository.findAll(articleSpecification(ids,category.getIsDesc(), ArticleServiceImpl.ArticleList.NO_INCLUDE_TOP),PageRequest.of(page,category.getArticleListSize()));
            Set<Integer> ids =  new HashSet<>();
            ids.add(category.getId());
            Page<Article> articles = articleService.pageArticleByCategoryIds(ids, null, pageRequest);
            Page<ArticleVO> articleVOS = articleService.convertToPageVo(articles);
            Map<String,Object> map = new HashMap<>();
            map.put("view",articleVOS);
            map.put("showUrl","/articleList?categoryId="+category.getId()+"&sort=visits,DESC");
            map.put("name",category.getName()+"推荐");
            Template template = templateService.findByEnName(CmsConst.ARTICLE_RECENT_LIST);
            TemplateUtil.convertHtmlAndSave(category.getPath()+CMSUtils.getArticleRecentPath(),category.getViewName(),map,template);
        });
    }



    @TemplateOptionMethod(name = "底部导航",templateValue = "components/@footer",viewName="footer")
    public Map<String, Object> footer() {
        return new HashMap<>();
    }
//
//    @TemplateOptionMethod(name = "底部导航2",templateValue = "components/$footer",viewName="footer")
//    public Map<String, Object> footer2() {
//        return new HashMap<>();
//    }

//    @TemplateOptionMethod(name = "首页组件2",templateValue = "components/$index",viewName="index",event = "ACAU" ,path="html")
//    public Map<String,Object> index2() {
//        Map<String,Object> map = new HashMap<>();
//        List<CategoryDto> categoryDtos = categoryService.listRecommend();
//        map.put("category",categoryDtos);
//        return map;
//    }



    @TemplateOptionMethod(name = "首页组件",templateValue = "components/@index",viewName="index",event = "ACAU" ,path="html")
    public Map<String,Object> index() {
        Map<String,Object> map = new HashMap<>();
        List<CategoryDto> categoryDtos = categoryService.listRecommend();
        map.put("category",categoryDtos);
        return map;
    }

    @TemplateOptionMethod(name = "分类菜单", templateValue = "components/@categoryList", viewName = "categoryMenu")
    public Map<String,Object> listCategory() {
        Map<String,Object> map = new HashMap<>();
        map.put("view",categoryService.listUserCategoryVo());
        return map;
    }

    @TemplateOptionMethod(name = "文献分类", templateValue = "components/@collectionTree", viewName = "collectionTree")
    public Map<String,Object> listCollection() {
        Map<String,Object> map = new HashMap<>();
        map.put("view",collectionService.listTree());
        return map;
    }

    @TemplateOptionMethod(name = "导航菜单",templateValue = "components/@header",viewName="header")
    public Map<String,Object> listMenu(){
        Map<String,Object> map = new HashMap<>();
        map.put("view", menuService.listVo());
        return  map;
    }

//    @TemplateOptionMethod(name = "导航菜单2",templateValue = "components/$header",viewName="header")
//    public Map<String,Object> listMenu2(){
//        Map<String,Object> map = new HashMap<>();
//        map.put("view", menuService.listVo());
//        return  map;
//    }
}
