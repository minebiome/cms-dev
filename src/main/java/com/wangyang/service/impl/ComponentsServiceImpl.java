package com.wangyang.service.impl;

import com.wangyang.common.CmsConst;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.exception.TemplateException;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.pojo.authorize.Role;
import com.wangyang.pojo.dto.ArticleDto;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.ArticleTags;
import com.wangyang.pojo.entity.Components;
import com.wangyang.pojo.entity.Tags;
import com.wangyang.pojo.entity.base.Content;
import com.wangyang.pojo.enums.CrudType;
import com.wangyang.pojo.params.ArticleQuery;
import com.wangyang.pojo.params.ComponentsParam;
import com.wangyang.config.ApplicationBean;
import com.wangyang.pojo.vo.ArticleVO;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.pojo.vo.ContentVO;
import com.wangyang.repository.ArticleTagsRepository;
import com.wangyang.repository.ComponentsRepository;
import com.wangyang.repository.base.BaseRepository;
import com.wangyang.service.IArticleService;
import com.wangyang.service.ICategoryService;
import com.wangyang.service.IComponentsService;
import com.wangyang.service.ITagsService;
import com.wangyang.service.base.AbstractCrudService;
import com.wangyang.service.base.IContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ComponentsServiceImpl extends AbstractCrudService<Components, Components, BaseVo,Integer> implements IComponentsService {


    @Autowired
    IArticleService articleService;

    @Autowired
    @Qualifier("contentServiceImpl")
    IContentService<Content,Content, ContentVO> contentService;


    @Autowired
    ICategoryService categoryService;
    @Autowired
    ITagsService tagsService;
    ComponentsRepository componentsRepository;
    @Autowired
    ArticleTagsRepository articleTagsRepository;
    public ComponentsServiceImpl(  ComponentsRepository componentsRepository) {
        super(componentsRepository);
        this.componentsRepository=componentsRepository;
    }


    @Override
    public Page<Components> list(Pageable pageable){
        return componentsRepository.findAll(pageable);
    }

    @Override
    public List<Components> listNeedArticle(){
        Specification<Components> specification = new Specification<Components>() {
            @Override
            public Predicate toPredicate(Root<Components> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("dataName"),"@Article")).getRestriction();
            }
        };
        return componentsRepository.findAll(specification);
    }

    @Override
    public List<Components> listAll() {
        return componentsRepository.findAll();
    }

    @Override
    public Components add(ComponentsParam componentsParam){
        Components components = new Components();
        convert(components,componentsParam);
        components.setIsSystem(false);
        return componentsRepository.save(components);
    }



    @Override
    public Components update(int id, ComponentsParam componentsParam){
        Components components = findById(id);
        convert(components,componentsParam);
        if(components.getEnName()==null){
            components.setEnName(CMSUtils.randomViewName());
        }
        return componentsRepository.save(components);
    }

    private void convert(Components components,ComponentsParam componentsParam){
        BeanUtils.copyProperties(componentsParam,components,"templateValue");
        String templateValue =componentsParam.getTemplateValue();
        if(templateValue.startsWith("templates")){
            String templateValueName = templateValue.split("\n")[0];
            String path = CmsConst.WORK_DIR+"/"+templateValueName+".html";
            File file = new File(path);
            String fileTemplateValue = componentsParam.getTemplateValue();
            components.setTemplateValue(templateValueName);
            String replaceFileTemplateValue = fileTemplateValue.replace(templateValueName+"\n", "");
            FileUtils.saveFile(file,replaceFileTemplateValue);
        }else {
            components.setTemplateValue(componentsParam.getTemplateValue());
        }
    }

    @Override
    public List<Components> addAll(List<Components> templatePages) {
        return componentsRepository.saveAll(templatePages);
    }

    @Override
    public Components findById(int id){
        Optional<Components> templatePageOptional = componentsRepository.findById(id);
        if(!templatePageOptional.isPresent()){
            throw new TemplateException("add template did't exist!!");
        }
        return templatePageOptional.get();
    }


    @Override
    public Components findDetailsById(int id){
        Components components = findById(id);
        String templateValue = components.getTemplateValue();
        if(templateValue.startsWith("templates")){
            String path = CmsConst.WORK_DIR+"/"+templateValue+".html";
            File file = new File(path);
            if(file.exists()){
                String openFile = FileUtils.openFile(file);
                components.setTemplateValue(components.getTemplateValue()+"\n"+openFile);
            }
        }
        return components;
    }

    @Override
    public Components delete(int id){
        Components components = findById(id);
        if(components.getIsSystem()){
            throw new ObjectException("系统内置模板不能删除");
        }
        componentsRepository.deleteById(id);
        return components;
    }

    @Override
    public void deleteAll() {
        componentsRepository.deleteAll();
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }


    @Override
    public Map<String ,Object> getModel(Components components) {
        try {
            if(components.getDataName().startsWith(CmsConst.ARTICLE_DATA)){
                Map<String,Object> map = new HashMap<>();
                map.put("view",contentService.listByComponentsId(components.getId()));
                return  map;


//                Set<Integer> ids = Arrays.asList(args).stream().map(a -> Integer.parseInt(a)).collect(Collectors.toSet());
//                String[] names = components.getDataName().substring(1).split("\\.");
//                String className = names[0];
//                String methodName = names[1];
//                Object bean = ApplicationBean.getBean(className);
//                Method method = bean.getClass().getMethod(methodName,Set.class);
//                Object o = method.invoke(bean,ids);
//                return o;
            }else if(components.getDataName().startsWith(CmsConst.CATEGORY_DATA)){

                Map<String,Object> map = new HashMap<>();
                map.put("view",categoryService.listByComponentsId(components.getId()));
                return  map;
            } else if (components.getDataName().startsWith("articleJob")){
                String[] names = components.getDataName().split("\\.");
                String className = names[0];
                String methodName = names[1];
                Object bean = ApplicationBean.getBean(className);
                Method method = bean.getClass().getMethod(methodName);
                Object o = method.invoke(bean);
                Map<String,Object> map = (Map<String,Object>)o;
                return map;

            }else if(components.getDataName().startsWith(CmsConst.ARTICLE_DATA_SORT)){
                String args = components.getDataName().substring(CmsConst.ARTICLE_DATA_SORT.length());
                Sort sort;
                if(args!=null||!"".equals(args)){
                    String[] argsArray = args.split(",");
                    String directionStr = argsArray[argsArray.length-1];
                    if(directionStr.equals("DESC")||directionStr.equals("ASC")){
                        Sort.Direction direction = Sort.Direction.valueOf(directionStr);
                        sort = Sort.by(direction, Arrays.copyOf(argsArray,argsArray.length-1));
                    }else {
                        sort = Sort.by( argsArray);
                    }
                }else {
                    sort = Sort.by(Sort.Order.desc("id"));
                }
                Page<Article> articles = articleService.pagePublishBy(PageRequest.of(0, 5, sort));
                Page<ArticleVO> articleVOS = articleService.convertToPageVo(articles);
                Map<String,Object> map = new HashMap<>();
                map.put("view",articleVOS);
                map.put("showUrl","/articleList?sort="+args); //likes,DESC
                map.put("name",components.getName());
                return map;
//                Template template = templateService.findByEnName(CmsConst.ARTICLE_LIST);
//                TemplateUtil.convertHtmlAndSave();
            }else if(components.getDataName().startsWith(CmsConst.ARTICLE_DATA_KEYWORD)){

                String args = components.getDataName().substring(CmsConst.ARTICLE_DATA_KEYWORD.length());
                ArticleQuery articleQuery = new ArticleQuery();
                articleQuery.setKeyword(args);
//                articleQuery.setHaveHtml(true);
                Page<Article> articles = articleService.pagePublishBy(PageRequest.of(0, 5, Sort.by(Sort.Order.desc("updateDate"))), articleQuery);
                Page<ArticleDto> pageDto = articleService.convertArticle2ArticleDto(articles);
                Map<String,Object> map = new HashMap<>();
                map.put("view",pageDto);
                map.put("showUrl","/articleList?keyword="+args); //
                map.put("name",components.getName());
                return map;
            }else if(components.getDataName().startsWith(CmsConst.ARTICLE_DATA_TAGS)) {
                String args = components.getDataName().substring(CmsConst.ARTICLE_DATA_TAGS.length());
                Optional<Tags> tags = tagsService.findBy(args);
                if(tags.isPresent()){
                    Page<ArticleDto> articleDtos = articleService.pageByTagId(tags.get().getId(), 5);
                    Map<String,Object> map = new HashMap<>();
                    map.put("view",articleDtos);
                    map.put("showUrl","/articleList?tagsId="+tags.get().getId());
                    map.put("name",components.getName());
                    return map;
                }
            }else if(components.getDataName().startsWith(CmsConst.ARTICLE_DATA_SORT_SIZE)){
                String args = components.getDataName().substring(CmsConst.ARTICLE_DATA_SORT_SIZE.length());
                if(args==null||"".equals(args)){
                    throw new ObjectException("数据参数不能为空！！");
                }
                String[] argsArray = args.split(",");
                List<String> argLists = Arrays.asList(argsArray);
                int size=5;
                Set<String> sortStr = new HashSet<>();
                Sort.Direction direction = Sort.Direction.DESC;
                for (String arg : argLists){
                    if(arg.startsWith("size_")){
                        size = Integer.parseInt(arg.replace("size_", ""));
                    }else  if(arg.startsWith("order_")){
                        String order = arg.replace("order_", "");
                        direction = Sort.Direction.valueOf(order);
                    }else if(arg.startsWith("sort_")){
                        String sort_ = arg.replace("sort_", "");
                        sortStr.add(sort_);
                    }
                }
                Sort sort= Sort.by(direction,sortStr.toArray(new String[]{}));
                String orderSort = sortStr.stream()
                        .collect(Collectors.joining(","))+","+direction.name();

                Page<Article> articles = articleService.pagePublishBy(PageRequest.of(0, size, sort));
                Page<ArticleVO> articleVOS = articleService.convertToPageVo(articles);
                Map<String,Object> map = new HashMap<>();
                map.put("view",articleVOS);
                map.put("showUrl","/articleList?sort="+orderSort); //likes,DESC
                map.put("name",components.getName());
                return map;
            } else if (components.getDataName().startsWith(CmsConst.TAG_DATA)) {
                List<Tags> tags = tagsService.listAll();
                Map<Tags,Integer> map =  new HashMap<>();
                for (Tags tag : tags){
                    List<ArticleTags> articleTags = articleTagsRepository.findByTagsId(tag.getId());
                    int size = articleTags.stream().filter(articleTag -> {
                        boolean b = articleTag.getTagsId() == tag.getId();
                        return b;
                    }).collect(Collectors.toSet()).size();
                    map.put(tag,size);
                }
                Map<Tags, Integer> sortMap = ServiceUtil.sortDescend(map);
                Set<Map.Entry<Tags, Integer>> entries = sortMap.entrySet();

                List<Tags> topTags = new ArrayList<>();

                int i=0;
                for(Map.Entry<Tags, Integer>  item : entries){
                    if(i>10){
                        break;
                    }
                    topTags.add(item.getKey());
                }

                Map<String,Object> model = new HashMap<>();
                model.put("view",topTags);
                return model;
            } else {

            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Components findByDataName(String dataName){
        List<Components> templatePages = componentsRepository.findAll(new Specification<Components>() {
            @Override
            public Predicate toPredicate(Root<Components> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("dataName"), dataName)
                        ,criteriaBuilder.isTrue(root.get("status"))).getRestriction();
            }
        });

        if(CollectionUtils.isEmpty(templatePages)){
            throw new TemplateException("Template Not found!!");
        }

        return templatePages.get(0);
    }

    @Override
    public Components findByViewName(String viewName){
        return componentsRepository.findByViewName(viewName);
    }

    @Override
    public Components findByEnName(String enName) {
        return componentsRepository.findByEnName(enName);
    }
}
