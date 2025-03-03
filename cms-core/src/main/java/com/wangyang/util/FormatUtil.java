package com.wangyang.util;

import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.Sheet;
import com.wangyang.pojo.entity.base.Content;
import com.wangyang.pojo.vo.ArticleDetailVO;
import com.wangyang.pojo.vo.CategoryVO;

import java.io.File;

/**
 * @author wangyang
 * @date 2020/12/15
 */
public class FormatUtil {
    /**
     * 第一页分类文章列表
     * eg. html_articleList_bioinfo.html
     * @param category
     * @return
     */
    public static String categoryListFormat(Category category){
        if(category.getPath().startsWith("html")){
            return File.separator+category.getPath().replace("html/","")+File.separator+category.getViewName()+".html";
        }
        return File.separator+category.getPath().replace(File.separator,"_")+"_"+category.getViewName()+".html";
    }

    public static String categoryListFormat(CategoryVO category){
        if(category.getPath().startsWith("html")){
            return File.separator+category.getPath().replace("html/","")+File.separator+category.getViewName()+".html";
        }
        return File.separator+category.getPath().replace(File.separator,"_")+"_"+category.getViewName()+".html";
    }
    /**
     * 第二页分类文章列表
     *  eg. html_articleList_bioinfo_2_page.html
     * @param category
     * @return
     */
    public static String categoryList2Format(Category category) {
        return File.separator+category.getPath().replace(File.separator,"_")+"_"+category.getViewName();
    }
    public static String categoryList2Format(CategoryVO category) {
        if(category.getPath().startsWith("html")){
            return File.separator+category.getPath().replace("html/","")+File.separator+category.getViewName();
        }
        return File.separator+category.getPath().replace(File.separator,"_")+"_"+category.getViewName();
    }

    public static String articleList2Format(Article article) {
        if(article.getPath().startsWith("html")){
            return File.separator+article.getPath().replace("html/","")+File.separator+article.getViewName();
        }
        return File.separator+article.getPath().replace(File.separator,"_")+"_"+article.getViewName();
    }
    public static String articleListFormat(Article article) {
        if(article.getPath().startsWith("html")){
            return File.separator+article.getPath().replace("html/","")+File.separator+article.getViewName()+".html";
        }
        return File.separator+article.getPath().replace(File.separator,"_")+"_"+article.getViewName()+".html";
    }
    public static String sheetListFormat(Sheet sheet) {
        if(sheet.getPath().startsWith("html")){
            return File.separator+sheet.getPath().replace("html/","")+File.separator+sheet.getViewName()+".html";
        }
        return File.separator+sheet.getPath().replace(File.separator,"_")+"_"+sheet.getViewName()+".html";
    }


    public static String articleListFormat(Content content) {
        if(content.getPath().startsWith("html")){
            return File.separator+content.getPath().replace("html/","")+File.separator+content.getViewName()+".html";
        }
        return File.separator+content.getPath().replace(File.separator,"_")+"_"+content.getViewName()+".html";
    }

    public static String articleFormat(ArticleDetailVO articleDetailVO) {
        if(articleDetailVO.getPath().startsWith("html")){
            return File.separator+articleDetailVO.getPath().replace("html/","")+File.separator+articleDetailVO.getViewName()+".html";
        }
        return File.separator+articleDetailVO.getPath().replace(File.separator,"_")+"_"+articleDetailVO.getViewName()+".html";
    }
}
