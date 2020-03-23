package com.wangyang.cms.controller;

import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.dto.CategoryArticleListDao;
import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.params.ArticleQuery;
import com.wangyang.cms.pojo.support.BaseResponse;
import com.wangyang.cms.service.IArticleService;
import com.wangyang.cms.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.data.domain.Sort.Direction.DESC;


@Controller
@RequestMapping("/articleList")
public class ArticleListController {
    @Autowired
    IArticleService articleService;

    @GetMapping("/category/{categoryId}")
    public ModelAndView articleListByCategory(@PathVariable("categoryId") Integer categoryId, Integer page){
        if(page<=0) page=1;
        if(page==null||categoryId==null){
            return new ModelAndView("error");
        }
        page = page-1;
        return articleService.getArticleListByCategory(categoryId,page);
    }


    /**
     * 基于AJax的分页
     * @param categoryId

     * @return
     */
    @GetMapping("/categoryAjax/{categoryId}")
    @ResponseBody
    public Page<ArticleDto> articleListByCategoryAjax(@PathVariable("categoryId") Integer categoryId, Integer page){
        ArticleQuery articleQuery = new ArticleQuery();
        articleQuery.setCategoryId(categoryId);
        Page<ArticleDto> articles = articleService.findArticleListByCategoryId(categoryId,page);
        return articles;
    }

    @GetMapping("/like/{id}")
    @ResponseBody
    public BaseResponse increaseLikes(@PathVariable("id") int id) {
        int likes = articleService.increaseLikes(id);
        Integer likesNumber = articleService.getLikesNumber(id);
        if(likes!=0&likesNumber!=null){
            return BaseResponse.ok("操作成功",likesNumber);
        }else {
            return BaseResponse.error("操作失败");
        }
    }

    @GetMapping("/visit/{id}")
    @ResponseBody
    public BaseResponse increaseVisits(@PathVariable("id") int id) {
        int visits = articleService.increaseVisits(id);
        Integer visitsNumber = articleService.getVisitsNumber(id);
        if(visits!=0&visitsNumber!=null){
            return BaseResponse.ok("操作成功",visitsNumber);
        }else {
            return BaseResponse.error("操作失败");
        }
    }

    @GetMapping("/getLike/{id}")
    @ResponseBody
    public  BaseResponse getLikesNumber(@PathVariable("id") int id){
        Integer likesNumber = articleService.getLikesNumber(id);
        if(likesNumber!=null){
            return BaseResponse.ok("成功获取文章浏览量",likesNumber);
        }
        return BaseResponse.error("获取文章浏览量失败");
    }
}
