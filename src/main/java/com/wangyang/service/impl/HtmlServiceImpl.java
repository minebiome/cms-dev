package com.wangyang.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangyang.common.CmsConst;
import com.wangyang.common.exception.ArticleException;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.common.utils.DocumentUtil;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.pojo.dto.ArticleAndCategoryMindDto;
import com.wangyang.pojo.dto.ArticleDto;
import com.wangyang.pojo.dto.CategoryArticleListDao;
import com.wangyang.pojo.entity.*;
import com.wangyang.pojo.enums.ArticleStatus;
import com.wangyang.pojo.vo.ArticleDetailVO;
import com.wangyang.pojo.vo.ArticleVO;
import com.wangyang.pojo.vo.CommentVo;
import com.wangyang.config.ApplicationBean;
import com.wangyang.repository.ArticleRepository;
import com.wangyang.repository.ComponentsRepository;
import com.wangyang.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Override
    @Async //异步执行
    public void conventHtml(ArticleDetailVO articleVO){
        if(articleVO.getStatus().equals(ArticleStatus.PUBLISHED)||articleVO.getStatus().equals(ArticleStatus.MODIFY)){
            Category category = articleVO.getCategory();
//            EntityCreatedEvent<Category> createArticle = new EntityCreatedEvent<>(category);
//            publisher.publishEvent(createArticle);
//            deleteTempFileByCategory(category);
            //生成文章列表，文章列表依赖分类列表
            convertArticleListBy(category);
            //判断评论文件是否存在
            if(!TemplateUtil.componentsExist(articleVO.getViewName())){
                generateCommentHtmlByArticleId(articleVO.getId());
            }
            //生成文章详情页面,依赖文章评论(在栏目页面文章详情依赖文章列表)
            covertHtml(articleVO);
            log.info("!!### generate "+articleVO.getViewName()+" html success!!");
            // 生成首页文章最新文章
//            generateNewArticle();
        }else {
            throw new ArticleException("文章状态不是PUBLISH或者MODIFY");
        }
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


    /**
     * 生成该栏目下文章列表, 只展示文章列表
     * @param category
     */
    @Override
    public CategoryArticleListDao convertArticleListBy(Category category) {
//        //生成分类列表,用于首页文章列表右侧展示
//        if(!TemplateUtil.componentsExist(category.getTemplateName())){
//                generateCategoryListHtml();
//        }
        Template template = templateService.findOptionalByEnName(category.getTemplateName());
        CategoryArticleListDao categoryArticle = articleService.findCategoryArticleBy(category,template, 0);
//        if(template.getTree()){
////            categoryArticle = articleService.findCategoryArticleBy(category);
//        }else {
//
//        }
        log.debug("生成"+category.getName()+"分类下的第一个页面!");
        String json = JSON.toJSON(categoryArticle).toString();
        TemplateUtil.saveFile(CMSUtils.getArticleListJs(),category.getViewName(),json,"json");
        String html = TemplateUtil.convertHtmlAndSave(CMSUtils.getCategoryPath(),categoryArticle.getViewName(),categoryArticle, template);

        //生成文章列表组件,用于首页嵌入
        String content = DocumentUtil.getDivContent(html, "#components");
        if(StringUtils.isNotEmpty(content)){
            TemplateUtil.saveFile(CMSUtils.getFirstArticleList(),category.getViewName(),content);
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
        return TemplateUtil.convertHtmlAndSave(CMSUtils.getCategoryPath(),viewName,categoryArticle,template);


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
    public CategoryArticleListDao convertArticleListBy(int categoryId){
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

        Map<String,Object> map = new HashMap<>();
        map = new HashMap<>();
        map.put("view",articleDetailVO);
        map.put("template",template);
        String html = TemplateUtil.convertHtmlAndSave(CMSUtils.getArticlePath(),articleDetailVO.getViewName(),map, template);
        return html;
    }


    @Override
    public void convertArticleListBy(Sheet sheet) {
        Template template = templateService.findByEnName(sheet.getTemplateName());
        TemplateUtil.convertHtmlAndSave(sheet,template);
    }
    @Override
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
        //只有在文章打开评论时才能生成评论
        if(article.getOpenComment()){
            List<CommentVo> commentVos = commentService.listVoBy(articleId);
            //获取文章评论的模板
            Template template = templateService.findByEnName(article.getCommentTemplateName());
            Map<String,Object> map = new HashMap<>();
            map.put("comments",commentVos);
            TemplateUtil.convertHtmlAndSave(CMSUtils.getComment(),article.getViewName(),map,template);

            String json = JSON.toJSON(commentVos).toString();
            TemplateUtil.saveFile(CMSUtils.getCommentJSON(),article.getViewName(),json,"json");
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
