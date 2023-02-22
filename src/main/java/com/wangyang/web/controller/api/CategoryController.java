package com.wangyang.web.controller.api;

import com.wangyang.common.BaseResponse;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.CategoryTags;
import com.wangyang.pojo.vo.ArticleVO;
import com.wangyang.pojo.vo.CategoryDetailVO;
import com.wangyang.repository.CategoryTagsRepository;
import com.wangyang.service.IArticleService;
import com.wangyang.service.ICategoryService;
import com.wangyang.service.IHtmlService;
import com.wangyang.pojo.dto.CategoryDto;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.params.CategoryParam;
import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.pojo.vo.CategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/category")
@Slf4j
public class CategoryController {

    @Autowired
    ICategoryService categoryService;

    @Autowired
    IArticleService articleService;
    @Autowired
    IHtmlService htmlService;

    @Autowired
    CategoryTagsRepository categoryTagsRepository;

    @GetMapping
    public List<CategoryDto> list(){
        return categoryService.listAllDto();
    }

    @PostMapping
    public Category add(@Valid @RequestBody CategoryParam categoryParam){
        Category category = new Category();

        BeanUtils.copyProperties(categoryParam,category);
        Category saveCategory = categoryService.create(category,categoryParam.getTagIds());


        //生成category列表Html
        htmlService.generateCategoryListHtml();
        if(saveCategory.getHaveHtml()){
            //生成文章第一页的列表
            htmlService.convertArticleListBy(saveCategory);
        }
        return saveCategory;
    }






    /**
     * 根据模板英文名查找category
     * @param categoryEnName
     * @param pageable
     * @return
     */
    @GetMapping("/template/{categoryEnName}")
    public Page<Category> pageBy(@PathVariable("categoryEnName") String categoryEnName,@PageableDefault(size = 50)  Pageable pageable){
        return categoryService.pageBy(categoryEnName,pageable);
    }
    @PostMapping("/save/{categoryId}")
    public Category save(@Valid @RequestBody CategoryParam categoryParam,@PathVariable("categoryId") Integer categoryId){
        Category category = categoryService.findById(categoryId);

        BeanUtils.copyProperties(categoryParam, category,CMSUtils.getNullPropertyNames(categoryParam));
        Category updateCategory = categoryService.update(category,categoryParam.getTagIds());

//        //更新Category列表
//        htmlService.generateCategoryListHtml();
//        if(updateCategory.getHaveHtml()){
//            //生成文章第一页的列表
//            htmlService.convertArticleListBy(category);
//        }
        return updateCategory;
    }
    @PostMapping("/update/{categoryId}")
    public Category update(@Valid @RequestBody CategoryParam categoryParam,@PathVariable("categoryId") Integer categoryId){
        Category category = categoryService.findById(categoryId);

        BeanUtils.copyProperties(categoryParam, category,CMSUtils.getNullPropertyNames(categoryParam));
        Category updateCategory = categoryService.update(category,categoryParam.getTagIds());

        //更新Category列表
        htmlService.generateCategoryListHtml();
        if(updateCategory.getHaveHtml()){
            //生成文章第一页的列表
            htmlService.convertArticleListBy(category);
        }
        return updateCategory;
    }
    @GetMapping("/listByComponentsId/{componentsId}")
    public List<CategoryVO> listByComponentsId(@PathVariable("componentsId") Integer componentsId){
        return  categoryService.listByComponentsId(componentsId);
    }

    @RequestMapping("/delete/{id}")
    public Category deleteById(@PathVariable("id") Integer id){
        Category category = categoryService.findById(id);
        List<Article> articleDtos = articleService.listArticleDtoBy(category.getId());
        if(articleDtos.size()!=0){
            throw new ObjectException("不能删除该分类，由于存在"+articleDtos.size()+"篇文章！");
        }
        categoryService.deleteById(category.getId());
        //删除分离的列表页面
        TemplateUtil.deleteTemplateHtml(category.getViewName(), category.getPath());
        log.info("### delete category" + category.getName());
        //重新生成分类的列表
        htmlService.generateCategoryListHtml();
        return category;
    }
    @RequestMapping("/find/{id}")
    public CategoryDetailVO findByID(@PathVariable("id") Integer id){
        Category category = categoryService.findById(id);
        CategoryDetailVO categoryDetailVO = categoryService.covertToDetailVO(category);
        return categoryDetailVO;
    }

    @GetMapping("/updateAll")
    public Set<String> updateAllCategoryHtml(@RequestParam(value = "more", defaultValue = "false") Boolean more){
        List<Category> categories = categoryService.listAll();
        categories.forEach(category -> {
            if(more){
                if(category.getTemplateName()==null){
                    category.setTemplateName(CmsConst.DEFAULT_CATEGORY_TEMPLATE);
                }
                if(category.getArticleTemplateName()==null){
                    category.setArticleTemplateName(CmsConst.DEFAULT_ARTICLE_TEMPLATE);
                }
                if(category.getDesc()==null){
                    category.setDesc(true);
                }
                if(category.getArticleListSize()==null){
                    category.setArticleListSize(10);
                }
                category.setPath(CMSUtils.getCategoryPath());
                categoryService.save(category);
            }
            htmlService.convertArticleListBy(category);
        });

        return ServiceUtil.fetchProperty(categories,Category::getName);
    }

    @GetMapping("/updateHtml/{id}")
    public Category updateHtmlById(@PathVariable("id")  int id){
        Category category = categoryService.findById(id);
        if(category.getHaveHtml()){
            htmlService.convertArticleListBy(category);
            return category;
        }
        return category;
    }

    @GetMapping("/recommendOrCancel/{id}")
    public Category recommendOrCancelHome(@PathVariable("id") Integer id){
        Category category = categoryService.recommendOrCancelHome(id);
        htmlService.generateHome();
        return category;
    }
    @GetMapping("/addOrRemoveToMenu/{id}")
    public Category addOrRemoveToMenu(@PathVariable("id") int id){
        Category category = categoryService.addOrRemoveToMenu(id);
//        htmlService.generateMenuListHtml();
        return category;
    }
    @GetMapping("/haveHtml/{id}")
    public Category haveHtml(@PathVariable("id") int id){
        Category category = categoryService.haveHtml(id);
        if(category.getHaveHtml()){
            htmlService.convertArticleListBy(category);
        }else{

            TemplateUtil.deleteTemplateHtml(category.getViewName(),category.getPath());
        }

        htmlService.generateCategoryListHtml();
//        htmlService.generateMenuListHtml();
        htmlService.generateHome();
        return category;
    }

//    @GetMapping("/list/{id}")
//    public List<CategoryDto> listBaseCategory(@PathVariable("id") int id){
//        return categoryService.listCategoryDtoByParent(id);
//    }


//    @GetMapping("/listByParentId/{id}")
//    public List<CategoryDto> listByParentId(@PathVariable("id") Integer id){
//        return categoryService.listCategoryDtoByParent(id);
//    }


    @GetMapping("/generateHtml/{id}")
    public Category generateHtml(@PathVariable("id") Integer id){
        Category category = categoryService.findById(id);
        htmlService.convertArticleListBy(category);
        return category;
    }

    @GetMapping("/listVoTree")
    public List<CategoryVO> listCategoryVo(){
        return categoryService.listAdminCategoryVo();
    }
    @PostMapping("/updatePos")
    public BaseResponse addPos(@RequestBody List<CategoryVO> categoryVOList){

        categoryService.updateOrder(categoryVOList);
        //重新生成分类的列表
        htmlService.generateCategoryListHtml();
        return BaseResponse.ok("success");
    }

    @GetMapping("/updateCategoryInComponentOrder")
    public Category updateCategoryInComponentOrder(@RequestParam Integer id, @RequestParam Integer order){
        Category category = categoryService.findById(id);
        category.setCategoryInComponentOrder(order);
        return categoryService.save(category);
    }

}
