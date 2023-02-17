package com.wangyang.service.impl;

import com.wangyang.common.CmsConst;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.pojo.entity.*;
import com.wangyang.pojo.entity.Collection;
import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.pojo.enums.CrudType;
import com.wangyang.pojo.enums.TaskStatus;
import com.wangyang.pojo.enums.TaskType;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.pojo.vo.CategoryVO;
import com.wangyang.repository.LiteratureRepository;
import com.wangyang.repository.base.BaseRepository;
import com.wangyang.service.*;
import com.wangyang.service.base.AbstractContentServiceImpl;
import com.wangyang.service.base.AbstractCrudService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LiteratureServiceImpl  extends AbstractContentServiceImpl<Literature, Literature, BaseVo> implements ILiteratureService {

    private LiteratureRepository literatureRepository;
    private ITaskService taskService;
    private ICollectionService collectionService;
    private ITemplateService templateService;
    private IComponentsService componentsService;

    public LiteratureServiceImpl(LiteratureRepository literatureRepository,
                                 ITaskService taskService,
                                 ICollectionService collectionService,
                                 ITemplateService templateService,
                                 IComponentsService componentsService) {
        super(literatureRepository);
        this.literatureRepository = literatureRepository;
        this.taskService =taskService;
        this.collectionService = collectionService;
        this.templateService = templateService;
        this.componentsService =componentsService;
    }

    @Override
    public List<Literature> listByKeys(Set<String> literatureStrIds) {
//        List<Literature> literatures = new ArrayList<>();

        if(literatureStrIds.size()==0){
            return new ArrayList<>();
        }
        List<Literature> literatures = literatureRepository.findAll(new Specification<Literature>() {
            @Override
            public Predicate toPredicate(Root<Literature> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.in(root.get("key")).value(literatureStrIds)).getRestriction();
            }
        });


        return literatures;
    }


    @Override
    public List<Literature> listByCollectionId(Integer collectionId) {
        return literatureRepository.findAll(new Specification<Literature>() {
            @Override
            public Predicate toPredicate(Root<Literature> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("categoryId"),collectionId)).getRestriction();
            }
        });
    }

    @Override
    public void generateHtml(int userId) {
        List<Literature> literatureList = listAll();
        List<Collection> collections = collectionService.listAll();
        Template template = templateService.findByEnName(CmsConst.DEFAULT_LITERATURE_TEMPLATE);

        Components components = componentsService.findByViewName("collectionTree");
        Object o = componentsService.getModel(components);
        TemplateUtil.convertHtmlAndSave(o, components);
        for (Collection collection:collections){
            List<Literature> subLiterature = literatureList.stream().filter(literature ->
                    literature.getCategoryId().equals(collection.getId())
            ).collect(Collectors.toList());
            Map<String,Object> map = new HashMap<>();
            map = new HashMap<>();
            map.put("view",subLiterature);
            map.put("template",template);
            String html = TemplateUtil.convertHtmlAndSave(CMSUtils.getLiteraturePath(),collection.getKey(),map, template);
        }
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
