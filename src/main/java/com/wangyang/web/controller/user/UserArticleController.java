package com.wangyang.web.controller.user;


import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.pojo.annotation.CommentRole;
import com.wangyang.pojo.entity.*;
import com.wangyang.service.*;
import com.wangyang.service.authorize.IUserService;
import com.wangyang.pojo.dto.ArticleAndCategoryMindDto;
import com.wangyang.pojo.dto.CategoryDto;
import com.wangyang.pojo.dto.UserDto;
import com.wangyang.pojo.params.ArticleQuery;
import com.wangyang.pojo.vo.ArticleDetailVO;
import com.wangyang.util.FormatUtil;
import com.wangyang.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequestMapping("/user")
public class UserArticleController {
    @Autowired
    IUserService userService;

    @Autowired
    IArticleService articleService;

    @Autowired
    ICategoryService categoryService;
    @Autowired
    ILiteratureService literatureService;
    @Autowired
    IHtmlService htmlService;

    @Autowired
    ITemplateService templateService;

    @Autowired
    ISheetService sheetService;


    @GetMapping("/write")
    public String writeArticle(){
//        int userId = AuthorizationUtil.getUserId(request);

//        Page<Article> articlePage = articleService.pageByUserId(userId, pageable);
//        model.addAttribute("view",articlePage);
//        System.out.println(userId);
        return "user/write";
    }

    @GetMapping("/edit/{id}")
    public String editArticle(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
        int userId = AuthorizationUtil.getUserId(request);//在授权时将userId存入request
        Article article = articleService.findByIdAndUserId(id, userId);
        ArticleDetailVO articleDetailVO = articleService.conventToAddTags(article);
//        ArticleDetailVO articleDetailVO = articleService.convert(article);
        model.addAttribute("view",articleDetailVO);
        return "user/write";
    }
    @GetMapping("/editSheet/{id}")
    public String editSheet(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
        int userId = AuthorizationUtil.getUserId(request);//在授权时将userId存入request
        Sheet sheet = sheetService.findById(id);
//        Article article = articleService.findByIdAndUserId(id, userId);
//        ArticleDetailVO articleDetailVO = articleService.conventToAddTags(article);
//        ArticleDetailVO articleDetailVO = articleService.convert(article);
        model.addAttribute("view",sheet);
        return "user/writeSheet";
    }

    @GetMapping("/literature")
    @Anonymous
    public String searchLiterature(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable, String keywords, Model model){
        Template template = templateService.findByEnName(CmsConst.DEFAULT_LITERATURE_TEMPLATE);
        Set<String> filed = new HashSet<>();
        filed.add("title");
        Page<Literature> literature = literatureService.pageBy(pageable, keywords,filed);
        model.addAttribute("view",literature);
        return template.getTemplateValue();
    }

    /**
     * 快速创建类别的文章
     * @param request
     * @return
     */
    @GetMapping("/write/{categoryId}")
    public String fastWriteArticle(@RequestParam(required = true) String title,HttpServletRequest request,@PathVariable("categoryId") Integer categoryId,Model model){
        if(title==null||title.equals("")){
            return "error";
        }
        int userId = AuthorizationUtil.getUserId(request);
        ArticleDetailVO articleDetailVO = fastWriteArticleHtml(categoryId, title, userId);

//        model.addAttribute("view",articleDetailVO);
        return "redirect:"+ FormatUtil.categoryListFormat(articleDetailVO.getCategory());
    }



    public ArticleDetailVO fastWriteArticleHtml(int categoryId,String title,int userId){
        Article article = new Article();
        article.setCategoryId(categoryId);
        article.setTitle(title);
        article.setOriginalContent("# 开始你的创作:"+title);
        article.setUserId(userId);
        ArticleDetailVO articleDetailVO = articleService.createArticleDetailVo(article,null);

        htmlService.conventHtml(articleDetailVO);
        FileUtils.remove(CmsConst.WORK_DIR+ File.separator+articleDetailVO.getCategory().getPath()+File.separator+articleDetailVO.getCategory().getViewName());

        return articleDetailVO;
    }

    @GetMapping("/writeDraft/{categoryId}")
    public String fastWriteDraftArticleHtml(@RequestParam(required = true) String title,HttpServletRequest request,@PathVariable("categoryId") Integer categoryId,Model model){
        if(title==null||title.equals("")){
            return "error";
        }
        int userId =AuthorizationUtil.getUserId(request);
        Article article= fastWriteDraftArticleHtml(categoryId, title, userId);

//        model.addAttribute("view",articleDetailVO);
        return "redirect:/user/articleList?categoryId="+categoryId;
    }
    public Article fastWriteDraftArticleHtml(int categoryId,String title,int userId){
        Article article = new Article();
        article.setCategoryId(categoryId);
        article.setTitle(title);
        article.setOriginalContent("# 开始你的创作:"+title);
        article.setUserId(userId);
        Article saveArticle = articleService.saveArticleDraft(article,false);


        return saveArticle;
    }






    @GetMapping("/info")
    @CommentRole
    public String info(HttpServletRequest  request,Model model){
        int userId = AuthorizationUtil.getUserId(request);
        UserDto userDto = userService.findUserDaoById(userId);
        model.addAttribute("view",userDto);
        return "user/info";
    }


    @GetMapping("/mindJs/{categoryId}")
    public String mindJs(@PathVariable("categoryId") int categoryId,Model model){
        ArticleAndCategoryMindDto articleAndCategoryMindDto = articleService.listArticleMindDto(categoryId);
        Category category = articleAndCategoryMindDto.getCategory();
        String mindFormat = articleService.jsMindFormat(articleAndCategoryMindDto);
        model.addAttribute("mind",mindFormat);
        model.addAttribute("category",category);
        return "user/mindJs";
    }


    @GetMapping("/delete/{id}")
    public String deleteArticle(HttpServletRequest request,@PathVariable("id") Integer id){
        int userId = AuthorizationUtil.getUserId(request);
        return "redirect:/user/articleList";
    }

    /**
     * 显示用户文章，包含草稿
     * @param request
     * @param model
     * @param articleQuery
     * @param pageable
     * @return
     */
    @GetMapping("/articleList")
    public String articleList(HttpServletRequest request, Model model, ArticleQuery articleQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        int userId = AuthorizationUtil.getUserId(request);

        Page<Article> articlePage = articleService.pageByUserId(userId, pageable,articleQuery);
//        model.addAttribute("view",articleService.convertToAddCategory(articlePage));
        model.addAttribute("view",articlePage);
        List<CategoryDto> categories = categoryService.listAllDto();
        model.addAttribute("categories",categories);
        if(articleQuery.getCategoryId()!=null){
            model.addAttribute("categoryId",articleQuery.getCategoryId());
        }


//        System.out.println(userId);
        return "user/articleList";
    }

}
