package com.wangyang.service.impl;

import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.pojo.authorize.User;
import com.wangyang.pojo.dto.CategoryContentListDao;
import com.wangyang.pojo.dto.TagsDto;
import com.wangyang.pojo.entity.*;
import com.wangyang.pojo.entity.base.Content;
import com.wangyang.pojo.enums.ArticleStatus;
import com.wangyang.pojo.enums.CrudType;
import com.wangyang.pojo.vo.CategoryVO;
import com.wangyang.pojo.vo.ContentDetailVO;
import com.wangyang.pojo.vo.ContentVO;
import com.wangyang.repository.ArticleTagsRepository;
import com.wangyang.repository.TagsRepository;
import com.wangyang.repository.base.ContentRepository;
import com.wangyang.service.ICategoryService;
import com.wangyang.service.IContentServiceEntity;
import com.wangyang.service.authorize.IUserService;
import com.wangyang.service.base.AbstractContentServiceImpl;
import com.wangyang.util.FormatUtil;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContentServiceImpl extends AbstractContentServiceImpl<Content,Content, ContentVO>  implements IContentServiceEntity {
    @Autowired
    TagsRepository tagsRepository;
    @Autowired
    ArticleTagsRepository articleTagsRepository;

    @Autowired
    IUserService userService;
    @Autowired
    ICategoryService categoryService;
    private ContentRepository<Content> contentRepository;
    public ContentServiceImpl(ContentRepository<Content> contentRepository) {
        super(contentRepository);
        this.contentRepository=contentRepository;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
    private List<Content> listByCategory(){
        List<Content> contents = contentRepository.findAll();
        return contents;
    }
    private Specification<Content> articleSpecification(Set<Integer> ids, Boolean isDesc, ArticleServiceImpl.ArticleList articleList){
        Specification<Content> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            predicates.add(criteriaBuilder.in(root.get("categoryId")).value(ids));
            if(articleList.equals(ArticleServiceImpl.ArticleList.INCLUDE_TOP)){
                predicates.add( criteriaBuilder.isTrue(root.get("top")));
                predicates.add(  criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), ArticleStatus.PUBLISHED),
                        criteriaBuilder.equal(root.get("status"), ArticleStatus.MODIFY)));

            }else if(articleList.equals(ArticleServiceImpl.ArticleList.NO_INCLUDE_TOP)){
                predicates.add( criteriaBuilder.isFalse(root.get("top")));
                predicates.add(  criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), ArticleStatus.PUBLISHED),
                        criteriaBuilder.equal(root.get("status"), ArticleStatus.MODIFY)));

            }else if(articleList.equals(ArticleServiceImpl.ArticleList.ALL_PUBLISH_MODIFY_ARTICLE)){
                predicates.add(  criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), ArticleStatus.PUBLISHED),
                        criteriaBuilder.equal(root.get("status"), ArticleStatus.MODIFY)));
            }else if(articleList.equals(ArticleServiceImpl.ArticleList.ALL_ARTICLE)){

            }
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
            if(isDesc!=null){
                if(isDesc){
//                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("updateDate")));
//                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("order")),criteriaBuilder.desc(root.get("id")));
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
                }else {
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
//                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("order")),criteriaBuilder.desc(root.get("id")));

//                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("updateDate")));
                }
            }

            return criteriaQuery.getRestriction();
        };
        return specification;
    }

    @Override
    public Page<Content> pageContentByCategoryIds(Set<Integer> ids, Boolean isDesc, PageRequest pageRequest){
        Page<Content> contents = contentRepository.findAll(articleSpecification(ids,isDesc, ArticleServiceImpl.ArticleList.NO_INCLUDE_TOP),pageRequest);
        return contents;
    }

    @Override
    public List<Content> listContentByCategoryIds(Set<Integer> ids, Boolean isDesc) {
        return contentRepository.findAll(articleSpecification(ids,isDesc, ArticleServiceImpl.ArticleList.NO_INCLUDE_TOP));
    }


    @Override
    public List<ContentVO> convertToListVo(List<Content> contents) {
//        List<Article> articles = articlePage.getContent();
        //Get article Ids
        Set<Integer> articleIds = ServiceUtil.fetchProperty(contents, Content::getId);

        List<ArticleTags> articleTags = articleTagsRepository.findAllByArticleIdIn(articleIds);

        Set<Integer> tagIds = ServiceUtil.fetchProperty(articleTags, ArticleTags::getTagsId);
        List<Tags> tags = tagsRepository.findAllById(tagIds);
        Map<Integer, Tags> tagsMap = ServiceUtil.convertToMap(tags, Tags::getId);
        Map<Integer,List<Tags>> tagsListMap = new HashMap<>();
        articleTags.forEach(
                articleTag->{
                    tagsListMap.computeIfAbsent(articleTag.getArticleId(),
                                    tagsId->new LinkedList<>())
                            .add(tagsMap.get(articleTag.getTagsId()));
                }

        );
        Set<Integer> userIds = ServiceUtil.fetchProperty(contents, Content::getUserId);
        List<User> users = userService.findAllById(userIds);

        Map<Integer, User> userMap = ServiceUtil.convertToMap(users, User::getId);
//        Set<Integer> categories = ServiceUtil.fetchProperty(articles, Article::getCategoryId);
//        List<CategoryDto> categoryDtos = categoryService.findAllById(categories).stream().map(category -> {
//            CategoryDto categoryDto = new CategoryDto();
//            BeanUtils.copyProperties(category, categoryDto);
//            return categoryDto;
//        }).collect(Collectors.toList());
//        Map<Integer, CategoryDto> categoryMap = ServiceUtil.convertToMap(categoryDtos, CategoryDto::getId);


        List<ContentVO> contentVOS  = contents.stream().map(content -> {
            ContentVO contentVO = new ContentVO();
            BeanUtils.copyProperties(content,contentVO);
            contentVO.setUser(userMap.get(content.getUserId()));

            if(content.getOrder()==null){
                contentVO.setOrder(0);
            }

//            if(categoryMap.containsKey(article.getCategoryId())){
//                articleVO.setCategory( categoryMap.get(article.getCategoryId()));
//
//            }
//            articleVO.setLinkPath(FormatUtil.articleListFormat(article));
            contentVO.setTags(Optional.ofNullable(tagsListMap.get(content.getId()))
                    .orElseGet(LinkedList::new)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(tag->{
                        TagsDto tagsDto = new TagsDto();
                        BeanUtils.copyProperties(tag,tagsDto);
                        return tagsDto;
                    })
                    .collect(Collectors.toList()));
//            articleVO.setTags(tagsListMap.get(article.getId()));
            return contentVO;
        }).collect(Collectors.toList());
        return contentVOS;
    }


    @Override
    public Page<ContentVO> convertToPageVo(Page<Content> contentPage) {
        List<Content> contents = contentPage.getContent();
        //Get article Ids
        Set<Integer> articleIds = ServiceUtil.fetchProperty(contents, Content::getId);

        List<ArticleTags> articleTags = articleTagsRepository.findAllByArticleIdIn(articleIds);

        Set<Integer> tagIds = ServiceUtil.fetchProperty(articleTags, ArticleTags::getTagsId);
        List<Tags> tags = tagsRepository.findAllById(tagIds);
        Map<Integer, Tags> tagsMap = ServiceUtil.convertToMap(tags, Tags::getId);
        Map<Integer,List<Tags>> tagsListMap = new HashMap<>();
        articleTags.forEach(
                articleTag->{
                    tagsListMap.computeIfAbsent(articleTag.getArticleId(),
                                    tagsId->new LinkedList<>())
                            .add(tagsMap.get(articleTag.getTagsId()));
                }

        );
        Set<Integer> userIds = ServiceUtil.fetchProperty(contents, Content::getUserId);
        List<User> users = userService.findAllById(userIds);

        Map<Integer, User> userMap = ServiceUtil.convertToMap(users, User::getId);
//        Set<Integer> categories = ServiceUtil.fetchProperty(articles, Article::getCategoryId);
//        List<CategoryDto> categoryDtos = categoryService.findAllById(categories).stream().map(category -> {
//            CategoryDto categoryDto = new CategoryDto();
//            BeanUtils.copyProperties(category, categoryDto);
//            return categoryDto;
//        }).collect(Collectors.toList());
//        Map<Integer, CategoryDto> categoryMap = ServiceUtil.convertToMap(categoryDtos, CategoryDto::getId);


        Page<ContentVO> contentVOS = contentPage.map(content -> {
            ContentVO contentVO = new ContentVO();
            BeanUtils.copyProperties(content,contentVO);
            contentVO.setUser(userMap.get(content.getUserId()));
//            if(categoryMap.containsKey(article.getCategoryId())){
//                articleVO.setCategory( categoryMap.get(article.getCategoryId()));
//
//            }
//            articleVO.setLinkPath(FormatUtil.articleListFormat(article));
            contentVO.setTags(Optional.ofNullable(tagsListMap.get(content.getId()))
                    .orElseGet(LinkedList::new)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(tag->{
                        TagsDto tagsDto = new TagsDto();
                        BeanUtils.copyProperties(tag,tagsDto);
                        return tagsDto;
                    })
                    .collect(Collectors.toList()));
//            articleVO.setTags(tagsListMap.get(article.getId()));
            return contentVO;
        });
        return contentVOS;
    }
    public void addChildIds( List<CategoryVO> categoryVOS, Integer id){
        List<Category> categories = categoryService.findByParentId(id);
        if(categories.size()==0){
            return;
        }
        categoryVOS.addAll(categoryService.convertToListVo(categories));
        if(categories.size()!=0){
            for (Category category:categories){
                addChildIds(categoryVOS,category.getId());
            }
        }

    }

    @Override
    public void addParentCategory(List<CategoryVO> categoryVOS, Integer parentId){
        if(parentId==0){
            return;
        }
        Category category = categoryService.findById(parentId);
        categoryVOS.add(0,categoryService.covertToVo(category));
        if(category.getParentId()!=0){
            addParentCategory(categoryVOS,category.getParentId());
        }

    }

    @Override
    public CategoryContentListDao findCategoryContentBy(CategoryVO category, Template template, int page){
        CategoryContentListDao articleListVo = new CategoryContentListDao();

        /**
         * 根据一组id查找article
         * **/
        Set<Integer> ids =new HashSet<>();
        List<CategoryVO> categoryVOS = new ArrayList<>();
        ids.add(category.getId());
        addChildIds(categoryVOS,category.getId());


        List<Category> categoryPartner = categoryService.findByParentId(category.getParentId());
        articleListVo.setPartner(categoryService.convertToListVo(categoryPartner));

        if(categoryVOS.size()!=0){
            ids.addAll(ServiceUtil.fetchProperty(categoryVOS, CategoryVO::getId));
            List<CategoryVO> categoryVOSTree = categoryService.listWithTree(categoryVOS,category.getId());
            articleListVo.setChildren(categoryVOSTree);
        }
        if(category.getParentId()!=0){
            // add forward parent
//            Category parentCategory = categoryService.findById(category.getParentId());
//            CategoryVO parentCategoryVO = categoryService.covertToVo(parentCategory);
//            articleListVo.setParentCategory(parentCategoryVO);
//
//
//            List<CategoryVO> categoryVOSParent = new ArrayList<>();
//            categoryVOSParent.add(parentCategoryVO);
//            addParentCategory(categoryVOSParent,parentCategoryVO.getParentId());

            // add first parent
            List<CategoryVO> categoryVOSParent = new ArrayList<>();
            addParentCategory(categoryVOSParent,category.getParentId());
            CategoryVO categoryVO = categoryVOSParent.get(0);
            articleListVo.setParentCategory(categoryVO);
            articleListVo.setParentCategories(categoryVOSParent);

        }


        List<ContentVO> contents;


        if(!template.getTree()){
//            Page<Article> articles = pageArticleByCategoryIds(articleSpecification(ids,category.getIsDesc(), ArticleServiceImpl.ArticleList.NO_INCLUDE_TOP),PageRequest.of(page,category.getArticleListSize()));
            Page<Content> contentsPage = pageContentByCategoryIds(ids, category.getIsDesc(), PageRequest.of(page, category.getArticleListSize()));
            Page<ContentVO> contentVOS = convertToPageVo(contentsPage);
            int totalPages = contentVOS.getTotalPages();
            int size = contentVOS.getSize();
            long totalElements = contentVOS.getTotalElements();
            articleListVo.setTotalPages(totalPages);
            articleListVo.setSize(size);
            articleListVo.setTotalElements(totalElements);
            contents = contentVOS.getContent();
        }else {
            contents=listVoTree(ids,category.getIsDesc());
        }

        articleListVo.setContents(contents);
        articleListVo.setCategory(category);
        articleListVo.setViewName(category.getViewName());
        articleListVo.setPath(category.getPath());
        /**
         * 分页路径的格式生成
         */
        articleListVo.setLinkPath(FormatUtil.categoryList2Format(category));
        return articleListVo;
    }

    @Override
    public List<ContentVO> listVoTree(Integer categoryId) {
        Category category = categoryService.findById(categoryId);
        Set<Integer> ids  = new HashSet<>();
        ids.add(category.getId());
        return listVoTree(ids,category.getDesc());
//        ArticleQuery articleQuery = new ArticleQuery();
//        articleQuery.setCategoryId(category.getId());
//        articleQuery.setDesc(category.getDesc());
//        Specification<Article> specification = buildPublishByQuery(articleQuery);
//        List<Article> articles = articleRepository.findAll(specification);
////                .stream().map(article -> {
////            ArticleVO articleVO = new ArticleVO();
////            BeanUtils.copyProperties(article, articleVO);
////            return articleVO;
////        }).collect(Collectors.toList());
//        List<ArticleVO> articleVOS = convertToListVo(articles);
//        List<ArticleVO> articleVOTree = super.listWithTree(articleVOS);
////        List<ArticleDto> listWithTree = listWithTree(articleDtos);
//        return articleVOTree;
    }
    @Override
    public List<ContentVO> listVoTree(Set<Integer> ids, Boolean isDesc) {

//        ArticleQuery articleQuery = new ArticleQuery();
//        articleQuery.setCategoryId(category.getId());
//        articleQuery.setDesc(category.getDesc());



        Specification<Content> specification =  articleSpecification(ids,isDesc, ArticleServiceImpl.ArticleList.NO_INCLUDE_TOP);
        List<Content> contents = contentRepository.findAll(specification);
//                .stream().map(article -> {
//            ArticleVO articleVO = new ArticleVO();
//            BeanUtils.copyProperties(article, articleVO);
//            return articleVO;
//        }).collect(Collectors.toList());
        List<ContentVO> contentVOS = convertToListVo(contents);
        List<ContentVO> contentVOTree = super.listWithTree(contentVOS);
//        List<ArticleDto> listWithTree = listWithTree(articleDtos);
        return contentVOTree;
    }

    @Override
    public void updateOrder(Integer id, List<ContentVO> contentVOS) {
        Category category = categoryService.findById(id);
        Set<Integer> ids= new HashSet<>();
        ids.add(category.getId());
        List<Content> contents = listContentByCategoryIds(ids, true);

//        List<Article> articles = listArticleByCategoryIds(category.getId());
        super.updateOrder(contents,contentVOS);
    }


    @Override
    public List<ContentVO> listArticleVOBy(String viewName){
        Category category = categoryService.findByViewName(viewName);
        Set<Integer> ids = new HashSet<>();
        ids.add(category.getId());
        List<Content> contents = contentRepository.findAll(articleSpecification(ids, true, ArticleServiceImpl.ArticleList.NO_INCLUDE_TOP), Sort.by("order"));
        List<ContentVO> contentVOS = convertToListVo(contents);
        return contentVOS;
    }


    @Override
    public ContentDetailVO updateCategory(Content content, Integer baseCategoryId) {
        Optional<Category> category = categoryService.findOptionalById(baseCategoryId);
        ContentDetailVO contentDetailVO = new ContentDetailVO();
        contentDetailVO.setContent(content);
        if(category.isPresent()){
            content.setCategoryId(category.get().getId());
            contentDetailVO.setCategory(category.get());
        }else {
            content.setCategoryId(0);
        }
        contentRepository.save(content);
        return contentDetailVO;


    }
}