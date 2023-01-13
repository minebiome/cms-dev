package com.wangyang.web.core.aop;

import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.vo.ArticleDetailVO;
import com.wangyang.service.ICategoryService;
import com.wangyang.service.IHtmlService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangyang
 * @date 2020/12/16
 */

@Component
@Aspect
@Slf4j
public class ArticleAspectJ {

    @Autowired
    IHtmlService htmlService;

    @Autowired
    ICategoryService categoryService;



    public boolean findArticleInCategory(String categoryViewName,String articleCategoryViewName){
        Category category = categoryService.findByViewName(categoryViewName);
        if(category==null){
            return false;
        }
        List<Category> categories = categoryService.findByParentId(category.getId());
        categories.add(category);
        for (Category c : categories){
            if(c.getViewName().equals(articleCategoryViewName)){
                return true;

            }
        }
        return false;

    }


    /**
     * 需要执行删除
     */
    @Around("execution(* com.wangyang.web.controller.api.ArticleController.updateArticleDetailVO(..)) or " +
            "execution(* com.wangyang.web.controller.api.ArticleController.createArticleDetailVO(..)) or " +
            "execution(* com.wangyang.web.controller.api.ArticleController.delete(..)) or " +
            "execution(* com.wangyang.web.controller.api.ArticleController.createArticleDetailVO(..)) or " +
//            "execution(* com.wangyang.web.controller.user.UserArticleController.fastWriteArticle(..)) or " +
            "execution(* com.wangyang.web.controller.api.ArticleController.updateCategory(..))")
    public ArticleDetailVO test(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object o = joinPoint.proceed();
            ArticleDetailVO articleDetailVO = (ArticleDetailVO)o;
            if(findArticleInCategory("technologyServices",articleDetailVO.getCategory().getViewName())){
                htmlService.generateMenuListHtml();
            }
            if(findArticleInCategory("news",articleDetailVO.getCategory().getViewName())){
                htmlService.generateHome();
            }


            deleteTemp(articleDetailVO.getCategory().getName(),articleDetailVO.getCategory().getViewName(),articleDetailVO.getCategory().getParentId());
            if(articleDetailVO.getOldCategory()!=null){
                deleteTemp(articleDetailVO.getOldCategory());
            }
            htmlService.newArticleListHtml();
            log.info(">>> "+articleDetailVO.getCategory().getName()+"的临时文件");
            return articleDetailVO;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 需要执行删除
     */
    @Around("execution(* com.wangyang.web.controller.api.CategoryController.update(..)) or " +
            "execution(* com.wangyang.web.controller.api.CategoryController.deleteById(..)) or " +
            "execution(* com.wangyang.web.controller.api.ContentController.updateCategory(..)) or " +
            "execution(* com.wangyang.web.controller.api.CategoryController.haveHtml(..)) or "+
            "execution(* com.wangyang.web.controller.api.CategoryController.generateHtml(..))")
    public Category categoryAop(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object o = joinPoint.proceed();
            Category category = (Category)o;
            if(category!=null){
                deleteTemp(category);
            }

            htmlService.generateMenuListHtml();
            return category;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }




    @Async
    public void deleteTemp(Category category){
        deleteTemp(category.getName(),category.getViewName(),category.getParentId());
    }

    @Async
    public void deleteTemp(String title,String viewName,Integer parentId){



        log.info(">>>>>>>>>>>>>>>>>####删除分类分页文件-"+title);
        File dir = new File(CmsConst.WORK_DIR+File.separator+ CMSUtils.getCategoryPath());
        File[] files = dir.listFiles();
        for(File file : files){
            String name = file.getName();
            Pattern pattern = Pattern.compile(viewName+"-(.*)-page.html");
            Matcher matcher = pattern.matcher(name);
            if(matcher.find()){
                file.delete();
            }
        }
        if(parentId!=0){
            Category parentCategory = categoryService.findById(parentId);
            deleteTemp(parentCategory);
        }
//        FileUtils.remove(CmsConst.WORK_DIR+File.separator+ CMSUtils.getCategoryPath()+category.getViewName()+"-");
        //移除临时文章分类
//        FileUtils.remove(CmsConst.WORK_DIR+"/html/articleList/queryTemp");
//        FileUtils.remove(CmsConst.WORK_DIR+"/html/mind/"+category.getId()+".html");
    }
}
