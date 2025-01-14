package com.wangyang.web.controller.api;

import com.wangyang.common.BaseResponse;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.base.Content;
import com.wangyang.pojo.vo.ArticleDetailVO;
import com.wangyang.pojo.vo.ArticleVO;
import com.wangyang.pojo.vo.ContentDetailVO;
import com.wangyang.pojo.vo.ContentVO;
import com.wangyang.service.ICategoryService;
import com.wangyang.service.IHtmlService;
import com.wangyang.service.base.IContentService;
import com.wangyang.util.AuthorizationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/content")
//@CrossOrigin
@Slf4j
public class ContentController {

    @Autowired
    @Qualifier("contentServiceImpl")
    IContentService<Content,Content, ContentVO> contentService;
    @Autowired
    ICategoryService categoryService;
    @Autowired
    IHtmlService htmlService;
    @GetMapping("/listVoTree/{categoryId}")
    public List<ContentVO> listDtoTree(@PathVariable("categoryId") Integer categoryId){
        List<ContentVO> listVoTree = contentService.listVoTree(categoryId);
        return listVoTree;
    }

    @PostMapping("/updatePos/{id}")
    public BaseResponse addPos(@PathVariable("id") Integer id, @RequestBody List<ContentVO> contentVOS){
        contentService.updateOrder(id,contentVOS);
        //重新生成分类的列表
//        htmlService.generateCategoryListHtml();
        return BaseResponse.ok("success");
    }


    /**
     * 更新文章分类
     * @param articleId
     * @param baseCategoryId
     * @return
     */
    @GetMapping("/updateCategory/{articleId}")
    public Category updateCategory(@PathVariable("articleId") Integer articleId, Integer baseCategoryId, HttpServletRequest request){
        int userId = AuthorizationUtil.getUserId(request);
        Content content = contentService.findById(articleId);
//        checkUser(userId,article);
//        String  viewName = article.getViewName();
//        String path = article.getPath();

        Integer categoryId=null;
        if(content.getCategoryId()!=null){
            categoryId = content.getCategoryId();
        }
        ContentDetailVO updateContent = contentService.updateCategory(content, baseCategoryId);
        //删除旧文章
//        TemplateUtil.deleteTemplateHtml(viewName,path);
        //更新旧的文章列表
        if(categoryId!=null&& categoryId!=0){
            Category oldCategory = categoryService.findById(categoryId);
//            articleDetailVO.setOldCategory(oldCategory);
            htmlService.convertArticleListBy(oldCategory);
            // 删除分页的文章列表
//            FileUtils.removeCategoryPageTemp(oldCategory);
        }

//        生成改变后文章
        if(updateContent.getCategory()!=null){
            htmlService.convertArticleListBy(updateContent.getCategory());
        }

//        htmlService.conventHtml(articleDetailVO);
        // 删除分页的文章列表
//        FileUtils.removeCategoryPageTemp(articleDetailVO.getCategory());
//        FileUtils.remove(CmsConst.WORK_DIR+"/html/articleList/queryTemp");

        return updateContent.getCategory();
    }

    @GetMapping("/listByComponentsId/{componentsId}")
    public List<ContentVO> listByComponentsId(@PathVariable("componentsId") Integer componentsId){
        return  contentService.listByComponentsId(componentsId);
    }
    @GetMapping("/updateArticleInComponentOrder")
    public Content updateArticleInComponentOrder(@RequestParam Integer id,@RequestParam Integer order){
        Content content = contentService.findById(id);
        content.setArticleInComponentOrder(order);
        return contentService.save(content);
    }
}


