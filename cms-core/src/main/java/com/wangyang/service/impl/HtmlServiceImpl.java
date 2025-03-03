package com.wangyang.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.wangyang.common.CmsConst;
import com.wangyang.common.exception.ArticleException;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.utils.*;
import com.wangyang.pojo.dto.ArticleDto;
import com.wangyang.pojo.dto.ArticlePageCondition;
import com.wangyang.pojo.dto.CategoryArticleListDao;
import com.wangyang.pojo.dto.CategoryContentListDao;
import com.wangyang.pojo.entity.*;
import com.wangyang.pojo.entity.base.Content;
import com.wangyang.pojo.enums.ArticleStatus;
import com.wangyang.common.enums.Lang;
import com.wangyang.pojo.enums.TemplateType;
import com.wangyang.pojo.vo.*;
import com.wangyang.config.ApplicationBean;
import com.wangyang.repository.ArticleRepository;
import com.wangyang.repository.ArticleTagsRepository;
import com.wangyang.repository.CategoryTagsRepository;
import com.wangyang.repository.ComponentsRepository;
import com.wangyang.service.*;
import com.wangyang.service.base.IContentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
@Slf4j
public class HtmlServiceImpl implements IHtmlService {

    @Autowired
    ITemplateService templateService;
    @Autowired
    ICategoryService categoryService;

    @Autowired
    ComponentsRepository componentsRepository;
    @Autowired
    IArticleService articleService;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    IOptionService optionService;
    @Autowired
    ISheetService sheetService;

    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    IComponentsService componentsService;
    @Autowired
    ICommentService commentService;

    @Autowired
    ArticleTagsRepository articleTagsRepository;
    @Autowired
    CategoryTagsRepository categoryTagsRepository;

    @Autowired
    @Qualifier("contentServiceImpl")
    IContentService<Content,Content, ContentVO> contentService;

    @Autowired
    IComponentsArticleService componentsArticleService;

    @Autowired
    IComponentsCategoryService componentsCategoryService;

    @Override
    @Async //异步执行
    public void conventHtml(ArticleDetailVO articleVO){
        if(articleVO.getStatus().equals(ArticleStatus.PUBLISHED)||articleVO.getStatus().equals(ArticleStatus.MODIFY)){
            CategoryVO categoryVO = articleVO.getCategory();

            List<CategoryVO> categoryVOS =new ArrayList<>();
            articleService.addParentCategory(categoryVOS,articleVO.getCategory().getParentId());
            articleVO.setParentCategory(categoryVOS);

//            List<Category> partnerCategory = categoryService.findByParentId(categoryVO.getParentId());
//            articleVO.setPartnerCategory(categoryService.convertToListVo(partnerCategory));

//            EntityCreatedEvent<Category> createArticle = new EntityCreatedEvent<>(category);
//            publisher.publishEvent(createArticle);
//            deleteTempFileByCategory(category);
            //生成文章列表，文章列表依赖分类列表'

            convertArticleListBy(categoryVO);
            //判断评论文件是否存在
            if(!TemplateUtil.componentsExist(articleVO.getViewName())){
                generateCommentHtmlByArticleId(articleVO.getId());
            }
            //生成文章详情页面,依赖文章评论(在栏目页面文章详情依赖文章列表)
            covertHtml(articleVO);
            if(articleVO.getIsPublisher()){
                SitePusher.push(articleVO.getLinkPath());
            }

            log.info("!!### generate "+articleVO.getViewName()+" html success!!");
            // 生成首页文章最新文章
//            generateNewArticle();
        }else {
            throw new ArticleException("文章状态不是PUBLISH或者MODIFY");
        }
    }


    @Override
    @Async
    public void generateRecommendArticle(List<Category> categories){
        for(Category category:categories){
            List<CategoryTags> categoryTags = categoryTagsRepository.findByCategoryId(category.getId());
            if(categoryTags.size()!=0){
                Set<Integer> tagIds = ServiceUtil.fetchProperty(categoryTags, CategoryTags::getTagsId);

                List<ArticleTags> articleTags = articleTagsRepository.findAllByTagsIdIn(tagIds);
                Set<Integer> articleIds = ServiceUtil.fetchProperty(articleTags, ArticleTags::getArticleId);
                Page<Article> articles = articleService.pageByIds(articleIds, 0, 5, null);
                List<Article> contents = articles.getContent();
                List<ArticleVO> articleVOS = articleService.convertToListVo(contents);
                Map<String,Object> map = new HashMap<>();
                map.put("articleVOS",articleVOS);
                if(category.getRecommendTemplateName()==null){
                    category.setRecommendTemplateName(CmsConst.ARTICLE_RECOMMEND_LIST);
                }
                Template template = templateService.findByEnName(category.getRecommendTemplateName());
                TemplateUtil.convertHtmlAndSave(category.getPath()+CMSUtils.getArticleRecommendPath(),category.getViewName(),map,template);
            }

        }
    }
    @Override
    public String articlePageCondition(Integer componentsId, Set<String> sortStr, String order, Integer page, Integer size){
        Components components = componentsService.findById(componentsId);
        Map<String,Object> map = new HashMap<>();
        ArticlePageCondition articlePageCondition = articleService.pagePublishBy(componentsId, sortStr, order, page, size);
        Page<Article> articles =articlePageCondition.getArticles();
        Page<ArticleVO> articleVOS = articleService.convertToPageVo(articles);
//                Map<String,Object> map = new HashMap<>();
        map.put("view",articleVOS);
        map.put("info",articlePageCondition);
//        map.put("showUrl","/articleList?sort="+orderSort); //likes,DESC
//        TemplateUtil.convertHtmlAndSave(category.getPath()+CMSUtils.getArticleRecommendPath(),category.getViewName(),map,template);
        Context context = new Context();
        context.setVariables(map);
        String html = TemplateUtil.getHtml(components.getTemplateValue(),context);
//        TemplateUtil.saveFile(path,viewName,html);
        return html;
    }
    @Override
    public String articlePageCondition(Integer componentId, Set<Integer> ids, Set<String> sortStr, String order, Integer page, Integer size){
        String url = "component_"+componentId+",category_"+Joiner.on(",").join(ids)+",sort_"+Joiner.on(",").join(sortStr)+",order_"+order+",page_"+(page+1)+",size_"+size;
        if(TemplateUtil.checkFileExist("html/components/"+componentId,url)){
            return TemplateUtil.openFile("html/components/"+componentId,url);
        }

        Components components = componentsService.findById(componentId);
        Map<String,Object> map = new HashMap<>();
        ArticlePageCondition articlePageCondition = articleService.pagePublishBy(ids, sortStr, order, page, size);
        Page<Article> articles =articlePageCondition.getArticles();
        Page<ArticleVO> articleVOS = articleService.convertToPageVo(articles);
        if(articleVOS.getContent().size()==0){
            throw new ObjectException("没有数据！！");
        }
//                Map<String,Object> map = new HashMap<>();

        map.put("view",articleVOS);
        map.put("info",articlePageCondition);

        map.put("url",url);
//        map.put("showUrl","/articleList?sort="+orderSort); //likes,DESC
//        TemplateUtil.convertHtmlAndSave(category.getPath()+CMSUtils.getArticleRecommendPath(),category.getViewName(),map,template);
        Context context = new Context();
        context.setVariables(map);
        String html = TemplateUtil.getHtml(components.getTemplateValue(),context);
        TemplateUtil.saveFile("html/components/"+componentId,url,html);
        return html;
    }
    /**
     * 从思维导图创建文章生成静态页面，之后统一生成文章首页列表
     * @param articleVO
     */
    @Override
    @Async //异步执行
    public void conventHtmlNoCategoryList(ArticleDetailVO articleVO){
        if(articleVO.getStatus()== ArticleStatus.PUBLISHED){

//            Category category = articleVO.getCategory();
            //生成文章列表，文章列表依赖分类列表
//            convertArticleListBy(category);

            //判断评论文件是否存在
            if(!TemplateUtil.componentsExist(articleVO.getViewName())){
                generateCommentHtmlByArticleId(articleVO.getId());
            }
            //生成文章详情页面,依赖文章评论(在栏目页面文章详情依赖文章列表)
            covertHtml(articleVO);
            log.info("!!### generate "+articleVO.getViewName()+" html success!!");


            // 生成首页文章最新文章
//            generateNewArticle();
            //创建/更新 文章-删除文章分页的缓存文件
            //TODO
//            FileUtils.removeCategoryPageTemp(articleVO.getCategory());
            //移除临时文章分类
            FileUtils.remove(CmsConst.WORK_DIR+"/html/articleList/queryTemp");
        }
    }


//    @Override
//    public void addOrRemoveArticleToCategoryListByCategoryId(int id) {
//        Optional<Category> optionalCategory = categoryService.findOptionalById(id);
//        if(optionalCategory.isPresent()){
//            convertArticleListBy(optionalCategory.get());
//        }
//    }

    public void findAllParentCategoryId(Integer categoryId,Set<Category> ids){
        if(categoryId==0){
            return;
        }
        Category category = categoryService.findById(categoryId);
        ids.add(category);
        findAllParentCategoryId(category.getParentId(),ids);
    }


    @Override
    public Set<Category> findAllCategoryPatent(Integer categoryParentId){
        Set<Category> categorySet = new HashSet<>();
        findAllParentCategoryId(categoryParentId,categorySet);
        return categorySet;
    }

    @Override
    @Async
    public void generateComponentsByCategory(Integer categoryId, Integer categoryParentId){
        Set<Integer> ids = new HashSet<>();
        ids.add(categoryId);
        Set<Category> categorySet = findAllCategoryPatent(categoryParentId);
        ids.addAll(ServiceUtil.fetchProperty(categorySet, Category::getId));

        List<ComponentsCategory> componentsCategoryList = componentsCategoryService.findByCategoryId(ids);
        List<Components> components1 = componentsService.listByIds(ServiceUtil.fetchProperty(componentsCategoryList, ComponentsCategory::getComponentId));
        components1.forEach(component -> {
            Map<String, Object> model = componentsService.getModel(component);
            TemplateUtil.convertHtmlAndSave(model, component);
        });
    }


    @Override
    public void generateComponentsByArticle(Integer articleId){
        List<ComponentsArticle> componentsArticleList = componentsArticleService.findByArticleId(articleId);
        Set<Integer> componentIds = ServiceUtil.fetchProperty(componentsArticleList, ComponentsArticle::getComponentId);
        List<Components> components = componentsService.listByIds(componentIds);
        components.forEach(component -> {
            Map<String, Object> model = componentsService.getModel(component);
            TemplateUtil.convertHtmlAndSave(model, component);
        });
    }

    @Override
    public CategoryContentListDao convertArticleListBy(Category category) {
        return convertArticleListBy(categoryService.covertToVo(category));
    }

    /**
     * 生成该栏目下文章列表, 只展示文章列表
     * @param category
     */
    @Override
    public CategoryContentListDao convertArticleListBy(CategoryVO category) {
//        //生成分类列表,用于首页文章列表右侧展示
//        if(!TemplateUtil.componentsExist(category.getTemplateName())){
//                generateCategoryListHtml();
//        }
        Template template = templateService.findOptionalByEnName(category.getTemplateName());
        CategoryContentListDao categoryArticle = contentService.findCategoryContentBy(category,template, 0);
//        CategoryContentListDao categoryArticle = contentService.findCategoryContentBy(categoryService.covertToVo(category),template,0);

//        if(template.getTree()){
////            categoryArticle = articleService.findCategoryArticleBy(category);
//        }else {
//
//        }
        Map<String,Object> map = new HashMap<>();
        List<Template> templates = templateService.findByChild(template.getId());
        for (Template templateChild : templates){
            if(templateChild.getTemplateType().equals(TemplateType.ARTICLE_LIST)  && templateChild.getArticleSize()!=null && templateChild.getArticleSize()!=0){
                List<ContentVO> contents = categoryArticle.getContents();
                int size= templateChild.getArticleSize();
                CategoryContentListDao newCategoryArticle = new CategoryContentListDao();
                BeanUtils.copyProperties(categoryArticle, newCategoryArticle);
                if(contents.size()>size){
                    List<ContentVO> newContents = new ArrayList<>();
                    for (int i = 0;i<size;i++){
                        newContents.add(contents.get(i));
                    }

                    newCategoryArticle.setContents(newContents);
                }
                TemplateUtil.convertHtmlAndSave(category.getPath()+File.separator+templateChild.getEnName(),newCategoryArticle.getViewName(),newCategoryArticle, templateChild);
            }else {
                TemplateUtil.convertHtmlAndSave(category.getPath()+File.separator+templateChild.getEnName(),categoryArticle.getViewName(),categoryArticle, templateChild);
            }

            map.put(templateChild.getEnName(),category.getPath()+File.separator+templateChild.getEnName()+File.separator+categoryArticle.getViewName());
        }


//        log.debug("生成"+category.getName()+"分类下的第一个页面!");
//        String json = JSON.toJSON(categoryArticle).toString();
//        TemplateUtil.saveFile(category.getPath()+CMSUtils.getArticleListJs(),category.getViewName(),json,"json");




        map.put("view",categoryArticle);

        String html = TemplateUtil.convertHtmlAndSave(category.getPath(),categoryArticle.getViewName(),map, template);
        //生成文章列表组件,用于首页嵌入
//        String content = DocumentUtil.getDivContent(html, "#components");
//        if(StringUtils.isNotEmpty(content)){
//            TemplateUtil.saveFile(category.getPath()+CMSUtils.getFirstArticleList(),category.getViewName(),content);
//        }
//        if(categoryArticle.getChildren()!=null && categoryArticle.getChildren().size()!=0){
//            String categoryChildren = DocumentUtil.getDivContent(html, "#categoryChildren");
//            if(StringUtils.isNotEmpty(categoryChildren)){
//                TemplateUtil.saveFile(category.getPath()+CMSUtils.getCategoryChildren(),category.getViewName(),categoryChildren);
//            }
//        }



        /*生成只有标题的第一页文章列表*/
//        Template templateTitleList = templateService.findOptionalByEnName(CmsConst.CATEGORY_TITLE);
//        List<ContentVO> articleVOS = categoryArticle.getContents();
//        TemplateUtil.convertHtmlAndSave(CMSUtils.getFirstArticleTitleList(),categoryArticle.getViewName(),articleVOS, templateTitleList);


       /**
        * 生成父类的文章列表
        * **/
       if(category.getParentId()!=0){
           Category parentCategory = categoryService.findById(category.getParentId());
           convertArticleListBy(categoryService.covertToVo(parentCategory));

       }
        return categoryArticle;
    }

    /**
     * 生成分页的缓存
     * @param category
     * @param page
     * @return
     */
    @Override
    public String convertArticleListBy(Category category, int page) {
        if(page<=0){
            return "Page is not exist!!";
        }
        Template template = templateService.findOptionalByEnName(category.getTemplateName());

        CategoryArticleListDao categoryArticle = articleService.findCategoryArticleBy(category,template, page-1);
//        Page<ArticleVO> articlePage = categoryArticle.getContents();
        if(page>categoryArticle.getTotalPages()){
            return "Page is not exist!!";
        }
        log.debug("生成"+category.getName()+"分类下的第["+page+"]个页面缓存!");
        String viewName =   category.getViewName()+"-"+String.valueOf(page)+"-page";
//            String viewName = String.valueOf(page);
        return TemplateUtil.convertHtmlAndSave(category.getPath(),viewName,categoryArticle,template);


    }

//    public String renderMindJs(int categoryId){
//        ArticleAndCategoryMindDto articleAndCategoryMindDto = articleService.listArticleMindDto(categoryId);
//        Category category = articleAndCategoryMindDto.getCategory();
//        String mindFormat = articleService.jsMindFormat(articleAndCategoryMindDto);
//        Template template = templateService.findByEnName(CmsConst.ARTICLE_JS_MIND);
//        Map<String,Object> map = new HashMap<>();
//        map.put("mind",mindFormat);
//        map.put("category",category);
//        String viewName = "";
//        return  TemplateUtil.convertHtmlAndSave(category.getPath(),viewName,map,template);
//    }



    /**
     * 生成单纯文章分页的缓存，没有分类
     * @return
     */
    @Override
    public String convertArticlePageBy(HttpServletRequest request, Page<ArticleDto> articleDtoPage, String viewName) {
//        log.debug("生成"+category.getName()+"分类下的第["+page+"]个页面缓存!");
        Template template = templateService.findOptionalByEnName(CmsConst.ARTICLE_PAGE);

        Map<String,Object> map = new HashMap<>();
        map.put("view",articleDtoPage);
        map.put("request",request);
        String path = "articleList/queryTemp";
        return TemplateUtil.convertHtmlAndSave(path,viewName,map,template);


    }

    @Override
    public String previewArticlePageBy(HttpServletRequest request, Page<ArticleDto> articleDtoPage) {
//        log.debug("生成"+category.getName()+"分类下的第["+page+"]个页面缓存!");
        Template template = templateService.findOptionalByEnName(CmsConst.ARTICLE_PAGE);

        Map<String,Object> map = new HashMap<>();
        map.put("view",articleDtoPage);
        map.put("request",request);
        return TemplateUtil.convertHtmlAndPreview(map,template);


    }





    @Override
    public CategoryContentListDao convertArticleListBy(int categoryId){
        Category category = categoryService.findById(categoryId);
        return convertArticleListBy(category);
    }

    /**
     * 生成文章详情页的静态页面
     * @param articleDetailVO
     * @return
     */
    private String covertHtml(ArticleDetailVO articleDetailVO) {
        Template template = templateService.findByEnName(articleDetailVO.getTemplateName());

//        CategoryVO category = articleDetailVO.getCategory();
//        Template categoryTemplate = templateService.findOptionalByEnName(category.getTemplateName());
//        List<Template> templates = templateService.findByChild(categoryTemplate.getId());
//        Map<String,Object> map = new HashMap<>();
//        for (Template templateChild : templates){
//            map.put(templateChild.getEnName(),CMSUtils.getCategoryPath()+File.separator+templateChild.getEnName()+File.separator+category.getViewName());
//        }


        Map<String,Object> map = new HashMap<>();
        map.put("view",articleDetailVO);
        map.put("template",template);
        String html = TemplateUtil.convertHtmlAndSave(articleDetailVO.getPath(),articleDetailVO.getViewName(),map, template);
        return html;
    }


    @Override
    public void convertArticleListBy(Sheet sheet) {
        Template template = templateService.findByEnName(sheet.getTemplateName());
        String html = TemplateUtil.convertHtmlAndSave(sheet, template);
        if(sheet.getCategoryId()!=null && sheet.getCategoryId()!=0){
            Category category = categoryService.findById(sheet.getCategoryId());
            convertArticleListBy(category);
//            generateMenuListHtml();
        }

        String content = DocumentUtil.getDivContent(html, "#fragment");
        if(StringUtils.isNotEmpty(content)){
            TemplateUtil.saveFile(CMSUtils.getComponentFragment(),sheet.getViewName(),content);
        }

    }
    @Override
    @Async //异步执行
    public void newArticleListHtml(){
        Components components = componentsService.findByViewName("newArticleIndex");
        Object data = componentsService.getModel(components);
        TemplateUtil.convertHtmlAndSave(data,components);
    }


    @Override
    public Components generateHome(){
        Components components = componentsService.findByViewName("index");
//        Components components = componentsService.findByDataName("articleJob.index");
        Object data = componentsService.getModel(components);
        TemplateUtil.convertHtmlAndSave(data,components);
        return components;
    }


    /**
     * 生成分类树的Html
     */
    @Override
    public void generateCategoryListHtml() {
        Components components = componentsService.findByViewName("categoryMenu");
        Object data = componentsService.getModel(components);
        TemplateUtil.convertHtmlAndSave(data,components);

//        //获取该列表所在的组
//        List<CategoryVO> categoryVOS = categoryService.listCategoryVo();
//        Template template = templateService.findByEnName(CmsConst.DEFAULT_CATEGORY_LIST);
//        TemplateUtil.convertHtmlAndSave(CMSUtils.getComponentsPath(),CmsConst.CATEGORY_MENU,categoryVOS,template);
    }


    @Override
    @Async
    public void generateMenuListHtml() {
        Components components = componentsService.findByDataName("articleJob.listMenu");
        Object data = componentsService.getModel(components);
        TemplateUtil.convertHtmlAndSave(data,components);
    }


    @Override
    public void commonTemplate(String option){
        List<Components> templatePages = componentsRepository.findAll((Specification<Components>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaQuery.where(
                        criteriaBuilder.like(root.get("event"), "%"+option+"%"),
                        criteriaBuilder.isTrue(root.get("status"))
                ).getRestriction());

        templatePages.forEach(templatePage -> {
            Object data = getData(templatePage.getDataName());
            if(data!=null){
                TemplateUtil.convertHtmlAndSave(data,templatePage);
            }
        });
    }

    public Object getData(String name){
        try {
            String[] names = name.split("\\.");
            String className = names[0];
            String methodName = names[1];
            Object bean = ApplicationBean.getBean(className);
            Method method = bean.getClass().getMethod(methodName);
            return method.invoke(bean);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据文章Id生成该文章评论的Html
     * @param articleId
     */
    @Override
    public void generateCommentHtmlByArticleId(int articleId){
        Article article = articleService.findArticleById(articleId);
        generateCommentHtmlByArticleId(article);
    }
    @Override
    public void generateCommentHtmlByArticleId(Article article){
        //只有在文章打开评论时才能生成评论
        if(article.getOpenComment()){
            List<CommentVo> commentVos = commentService.listVoBy(article.getId());
            //获取文章评论的模板
            Template template = templateService.findByEnName(article.getCommentTemplateName());
            Map<String,Object> map = new HashMap<>();
            map.put("comments",commentVos);
            map.put("viewName",article.getViewName());
            map.put("articleId",article.getId());
            TemplateUtil.convertHtmlAndSave(article.getPath()+CMSUtils.getComment(),article.getViewName(),map,template);

            String json = JSON.toJSON(commentVos).toString();
            TemplateUtil.saveFile(article.getPath()+CMSUtils.getCommentJSON(),article.getViewName(),json,"json");
        }

    }
    @Override
    public void generateComponentsByViewName(String path, String viewName) {
        Components components = componentsService.findByViewName(path, viewName);
        if(components!=null){
            Object data = componentsService.getModel(components);
            TemplateUtil.convertHtmlAndSave(data,components);
        }else {
            throw new ObjectException("组件 path:"+path+","+"viewName:"+viewName+"不存在！");
        }

    }

    @Override
    public void generateHtmlByViewName(String type, String viewName){
        Lang lang;
        if(type.startsWith("en")){
            lang=Lang.EN;
        }else {
            lang = Lang.ZH;
        }
        if(type.contains("sheet")){
            Sheet sheet = sheetService.findByViewName(viewName, lang);
            if(sheet==null){
                throw new ObjectException(viewName+"不存在！！");
            }
            convertArticleListBy(sheet);
        }else if (type.contains("articleList")){
            Category category = categoryService.findByViewName(viewName,lang);
            if(category==null){
                throw new ObjectException(viewName+"不存在！！");
            }
            convertArticleListBy(category);
        } else if (type.contains("article")) {
            Article article = articleService.findByViewName(viewName, lang);
            if(article==null){
                throw new ObjectException(viewName+"不存在！！");
            }
            ArticleDetailVO articleDetailVO = articleService.convert(article);
            conventHtml(articleDetailVO);

        }else {
            throw new ObjectException("type:"+type+","+"viewName:"+viewName+"不存在！");
        }

    }

    @Override
    public void articleTopListByCategoryId(int id) {
        Category category = categoryService.findById(id);
        List<ArticleDto> articleDtos = articleService.listTopByCategoryId(category);
//        if(articleDtos.size()==0)return;
        Template template = templateService.findByEnName(CmsConst.ARTICLE_TOP_LIST);
        Map<String,Object> map = new HashMap<>();
        map.put("view",articleDtos);
        map.put("category",category);
        String path = category.getPath()+ File.separator+"top";
        TemplateUtil.convertHtmlAndSave(path,category.getViewName(),map,template);
        convertArticleListBy(category);
    }

}
