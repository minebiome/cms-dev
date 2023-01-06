package com.wangyang.service.impl;

import com.wangyang.common.CmsConst;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.exception.OptionException;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.pojo.dto.CategoryDto;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.Template;
import com.wangyang.pojo.enums.CrudType;
import com.wangyang.pojo.params.CategoryQuery;
import com.wangyang.pojo.vo.CategoryVO;
import com.wangyang.repository.CategoryRepository;
import com.wangyang.service.*;
import com.wangyang.service.base.AbstractBaseCategoryServiceImpl;
import com.wangyang.util.FormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
//@TemplateOption
@Transactional
@Slf4j
public class CategoryServiceImpl extends AbstractBaseCategoryServiceImpl<Category,Category,CategoryVO> implements ICategoryService
        {


    @Autowired
    ITemplateService templateService;
    @Autowired
    IArticleService articleService;

    @Autowired
    IOptionService optionService;
    @Autowired
    IMenuService menuService;

    private  CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository =categoryRepository;
    }

    @Override
    public Category save(Category category){
        return categoryRepository.save(category);
    }
    @Override
    public Category create(Category categoryParam) {

        Category category = createOrUpdate(categoryParam);
        return category;
    }

    @Override
    public Category update(Category categoryParam) {
        Category category = createOrUpdate(categoryParam);
        return category;
    }

    public Category createOrUpdate(Category category){
        if(category.getParentId()==null){
            category.setParentId(0);
        }
        if(category.getOrder()==null){
            category.setOrder(0);
        }
        if(category.getArticleListSize()==null){
            category.setArticleListSize(10);
        }
        if (StringUtils.isEmpty(category.getViewName())) {
            String viewName = CMSUtils.randomViewName();
            category.setViewName(viewName);
        }

        if(category.getHaveHtml()==null){
            category.setHaveHtml(true);
        }

        if(category.getTemplateName()==null||"".equals(category.getTemplateName())){
            category.setTemplateName(CmsConst.DEFAULT_CATEGORY_TEMPLATE);
        }
        if(category.getArticleTemplateName()==null||"".equals(category.getArticleTemplateName())){
            category.setArticleTemplateName(CmsConst.DEFAULT_ARTICLE_TEMPLATE);
        }
        if(category.getDesc()==null){
            category.setDesc(true);
        }

        category.setPath(CMSUtils.getCategoryPath());

        Category saveCategory = categoryRepository.save(category);
        return saveCategory;
    }

    @Override
    public Page<Category> pageBy(String categoryEnName,Pageable pageable){

        Specification<Category> specification = new Specification<Category>() {
            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                criteriaQuery.where(criteriaBuilder.equal(root.get("templateName"),categoryEnName));
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("order")));

                return criteriaQuery.getRestriction();
            }
        };
        return  categoryRepository.findAll(specification,pageable);
    }


    @Override
    public List<CategoryDto> listBy(String categoryEnName){
        Sort sort = Sort.by(Sort.Order.desc("order")).and(Sort.by(Sort.Order.desc("id")));
        Specification<Category> specification = new Specification<Category>() {
            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("templateName"),categoryEnName);
            }
        };
        return  categoryRepository.findAll(specification,sort).stream().map(category -> {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(category,categoryDto);
            return categoryDto;
        }).collect(Collectors.toList());

    }

    /**
     * 显示推荐的首页并且是生成html的
     * @return
     */
    @Override
    public List<CategoryDto> listRecommend(){
        Sort sort = Sort.by(Sort.Order.desc("order")).and(Sort.by(Sort.Order.desc("id")));
        CategoryQuery categoryQuery = new CategoryQuery();
        categoryQuery.setHaveHtml(true);
        categoryQuery.setRecommend(true);
        List<Category> categories = list(categoryQuery,sort );
        return categories.stream().map(category -> {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(category, categoryDto);
            return categoryDto;
        }).collect(Collectors.toList());
    }


    @Override
    public List<Category> findAllById(Iterable<Integer> ids){
        return categoryRepository.findAllById(ids);
    }

    /**
     * 重新生成所有Category的Html
     *
     * @return
     */
//    @Override
//    public List<Integer> updateAllCategoryHtml() {
//        List<Category> categories = categoryRepository.findAll();
//        categories.forEach(category -> {
//            cmsService.generateCategoryArticleListByCategory(category);
//        });
//        return ids;
//    }



    @Override
    public Category deleteById(int id) {
        Category category = findById(id);
        List<Article> articleDtos = articleService.listArticleDtoBy(category.getId());
        if(articleDtos.size()!=0){
            throw new ObjectException("不能删除该分类，由于存在"+articleDtos.size()+"篇文章！");
        }
        categoryRepository.deleteById(id);
        return category;
    }






    @Override
    public Category findById(int id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        }

        throw new ObjectException("Category not found");
    }

    @Override
    public Optional<Category> findOptionalById(int id){
        return categoryRepository.findById(id);
    }



//    @Override
//    public CategoryArticleListDao findCategoryDetailVOByID(int id) {
//        Category category = findById(id);
//        return  getArticleListByCategory(category);
//
//    }

    //未使用
//    @Override
//    public Page<CategoryVO> list(Pageable pageable) {
//        Page<Category> categoryPage = categoryRepository.findAll(pageable);
//        return categoryPage.map(category -> {
//            CategoryVO categoryVO = new CategoryVO();
//            BeanUtils.copyProperties(category, categoryVO);
//            return categoryVO;
//        });
//    }





    @Override
    public List<Category> list(CategoryQuery categoryQuery,Sort sort) {
        Specification<Category> specification = new Specification<Category>() {
            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
               if(categoryQuery!=null){
                   List<Predicate> predicates = new LinkedList<>();
                   if (categoryQuery.getRecommend() != null) {
                       predicates.add( criteriaBuilder.isTrue(root.get("recommend")));
                   }
                   if(categoryQuery.getHaveHtml()!=null){
                       predicates.add( criteriaBuilder.isTrue(root.get("haveHtml")));
                   }
                   if(categoryQuery.getParentId()!=null){
                       predicates.add(criteriaBuilder.equal(root.get("parentId"),categoryQuery.getParentId()));
                   }
                   return  criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
               }
               return null;
            }
        };
        List<Category> categories = categoryRepository.findAll(specification,sort);
        return categories;
    }


    /**
     * 显示所有Category, 转化为Dto
     * @return
     */
    @Override
    public List<CategoryDto> listAllDto() {
        List<Category> categories = listAll();
        return convertTo(categories);
    }

    @Override
    public List<Category> listAll(){
        return categoryRepository.findAll(new Specification<Category>() {
            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return  criteriaQuery.where(criteriaBuilder.isTrue(root.get("haveHtml"))).getRestriction();
            }
        });
    }

    //TODO
    @Override
    public List<CategoryVO> listUserCategoryVo() {
        List<Category> categories = listAll();
        List<CategoryVO> categoryVOS = convertCategory2CategoryVO(categories);
        return categoryVOS;
    }
    @Override
    public List<CategoryVO> listAdminCategoryVo() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryVO> categoryVOS = convertCategory2CategoryVO(categories);
        return categoryVOS;
    }

    public List<CategoryVO> findTree(List<CategoryVO> categoryVOS){
        //根节点
        List<CategoryVO> rootCategory = new ArrayList<CategoryVO>();
        for (CategoryVO categoryVO : categoryVOS) {
            if (categoryVO.getParentId().equals(0)){ //父节点是0的，为根节点。
                rootCategory.add(categoryVO);
            }
        }
        /* 根据Menu类的order排序 */
        Collections.sort(rootCategory,categoryOrder() );
        //为根菜单设置子菜单，getClild是递归调用的
        for (CategoryVO categoryVO : rootCategory) {
            /* 获取根节点下的所有子节点 使用getChild方法*/
            List<CategoryVO> childList = getChild(categoryVO.getId(), categoryVOS);
            categoryVO.setChildren(childList);//给根节点设置子节点
        }
        return rootCategory;
    }
    /**
     * 获取子节点
     * @param id 父节点id
     * @param categoryVOS 所有菜单列表
     * @return 每个根节点下，所有子菜单列表
     */
    public List<CategoryVO> getChild(int id,List<CategoryVO> categoryVOS){
        //子菜单
        List<CategoryVO> childList = new ArrayList<CategoryVO>();
        for (CategoryVO categoryVO : categoryVOS) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if (categoryVO.getParentId().equals(id)){
                childList.add(categoryVO);
            }
        }
        //递归
        for (CategoryVO categoryVO : childList) {
            categoryVO.setChildren(getChild(categoryVO.getId(), categoryVOS));
        }
        Collections.sort(childList,categoryOrder()); //排序
        //如果节点下没有子节点，返回一个空List（递归退出）
        if (childList.size() == 0 ){
            return new ArrayList<CategoryVO>();
        }
        return childList;
    }


    public Comparator<CategoryVO> categoryOrder(){

        return new Comparator<CategoryVO>() {
            @Override
            public int compare(CategoryVO o1, CategoryVO o2) {
                return o1.getOrder()-o2.getOrder();
            }
        };
    }



    public List<CategoryVO> convertCategory2CategoryVO(List<Category> categories){
        List<CategoryVO> collect = categories.stream().map(category -> {
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category, categoryVO);
            categoryVO.setLinkPath(FormatUtil.categoryListFormat(category));
            return categoryVO;
        }).collect(Collectors.toList());
        return super.listWithTree(collect);
    }

//
//    public List<CategoryVO> listWithTree(List<CategoryVO> list) {
//        // 1. 先查出所有数据
////        List<ProjectLeader> list = projectLeaderService.list(Condition.getLikeQueryWrapper(projectLeader));
//        List<CategoryVO> collect = list.stream()
//                // 2. 找出所有顶级（规定 0 为顶级）
//                .filter(o -> o.getParentId().equals(0))
//                // 3.给当前父级的 childList 设置子
//                .peek(o -> o.setChildCategories(getChildList(o, list)))
//                .sorted(Comparator.comparing(CategoryVO::getOrder))
//                // 4.收集
//                .collect(Collectors.toList());
//        return collect;
//    }
//
//    // 根据当前父类 找出子类， 并通过递归找出子类的子类
//    private List<CategoryVO> getChildList(CategoryVO categoryVO, List<CategoryVO> list) {
//        return list.stream()
//                //筛选出父节点id == parentId 的所有对象 => list
//                .filter(o -> o.getParentId().equals(categoryVO.getId()))
//                .peek(o -> o.setChildCategories(getChildList(o, list)))
//                .sorted(Comparator.comparing(CategoryVO::getOrder))
//                .collect(Collectors.toList());
//    }




    public List<CategoryDto> convertTo(List<Category> categories){
        List<Template> templates = templateService.findAll();
        Map<String, Template> templateMap = ServiceUtil.convertToMap(templates, Template::getEnName);

        return  categories.stream().map(category -> {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setResource(templateMap.get(category.getArticleTemplateName()).getResource());
            BeanUtils.copyProperties(category, categoryDto);
            return categoryDto;
        }).collect(Collectors.toList());
    }


    /**
     * 不显示没有生成Html的category
     * @return
     */
//    @Override
////    @TemplateOptionMethod(name = "Category List", templateValue = "templates/components/@categoryList", viewName = "categoryList", path = "components")
//    public List<Category> list() {
//        CategoryQuery categoryQuery = new CategoryQuery();
//        categoryQuery.setHaveHtml(true);
//        return list(categoryQuery,Sort.by(Sort.Order.desc("order")).and(Sort.by(Sort.Order.desc("id"))));
//    }


    /**
     * 推荐分类到首页
     * @param id
     * @return
     */
    @Override
    public Category recommendOrCancelHome(int id){
        Category category = findById(id);
        if(category.getRecommend()){
            category.setRecommend(false);
        }else{
            if(!category.getHaveHtml()){
                throw new OptionException("在没有html关闭下，不能推荐到主页！");
            }
            category.setRecommend(true);
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category haveHtml(int id){
        Category category = findById(id);
        if(category.getHaveHtml()){
            category.setExistNav(false);
            category.setRecommend(false);
            category.setHaveHtml(false);
            menuService.removeCategoryToMenu(category.getId());
        }else{
            category.setHaveHtml(true);
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category addOrRemoveToMenu(int id) {
        Category category = findById(id);
        if(category.getExistNav()){
            category.setExistNav(false);
            menuService.removeCategoryToMenu(category.getId());
        }else {
            if(!category.getHaveHtml()){
                throw new OptionException("在没有html关闭下，不能添加到导航！");
            }
            category.setExistNav(true);
            menuService.addCategoryToMenu(category);
        }
        return categoryRepository.save(category);
    }

    @Override
    public List<CategoryVO> convertToListVo(List<Category> categories) {
        return categories.stream().map(category -> {
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category, categoryVO);
            categoryVO.setLinkPath(FormatUtil.categoryListFormat(category));
            return categoryVO;
        }).collect(Collectors.toList());
    }


    @Override
    public CategoryVO covertToVo(Category category){
        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(category, categoryVO);
        categoryVO.setLinkPath(FormatUtil.categoryListFormat(category));
        return categoryVO;
    }
            @Override
    public List<CategoryVO> listChildByViewName(String viewName){
        Category category = findByViewName(viewName);
        List<Category> categories = findByParentId(category.getId());
        return convertToListVo(categories);
    }


    @Override
    public Category findByViewName(String viewName){
        return categoryRepository.findByViewName(viewName);
    }


//    @Override
//    public void updateOrder(List<CategoryVO> categoryVOList) {
//        List<Category> saveCategories = new ArrayList<>();
//        List<Category> categories = categoryRepository.findAll();
//        Map<Integer, Category> categoryMap = ServiceUtil.convertToMap(categories, Category::getId);
//
//        updateOrder(categoryMap,saveCategories,categoryVOList,0);
//        categoryRepository.saveAll(saveCategories);
//    }

//    private void updateOrder(Map<Integer, Category> categoryMap ,List<Category> saveCategories,List<CategoryVO> categoryVOList, Integer pId) {
//
//        for(int i=0;i<categoryVOList.size();i++){
//            CategoryVO categoryVO = categoryVOList.get(i);
//            Category category = categoryMap.get(categoryVO.getId());
//            category.setOrder(i);
//            category.setParentId(pId);
//            saveCategories.add(category);
//
//            List<CategoryVO> childCategories = categoryVO.getChildCategories();
//            if(childCategories.size()>0){
//                updateOrder(categoryMap,saveCategories,childCategories,category.getId());
//            }
//
//        }
//    }


    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}