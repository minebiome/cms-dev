package com.wangyang.service.impl;

import com.wangyang.pojo.entity.CategoryTags;
import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.pojo.enums.CrudType;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.repository.CategoryTagsRepository;
import com.wangyang.service.ICategoryTagsService;
import com.wangyang.service.base.AbstractCrudService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

@Service
public class CategoryTagsServiceImpl extends AbstractCrudService<CategoryTags, BaseEntity, BaseVo,Integer> implements ICategoryTagsService {

    CategoryTagsRepository categoryTagsRepository;
    public CategoryTagsServiceImpl(CategoryTagsRepository categoryTagsRepository) {
        super(categoryTagsRepository);
        this.categoryTagsRepository =categoryTagsRepository;
    }

    @Override
    public List<CategoryTags> listByTagIds(Set<Integer> ids){
        List<CategoryTags> categoryTags = categoryTagsRepository.findAll(new Specification<CategoryTags>() {
            @Override
            public Predicate toPredicate(Root<CategoryTags> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                return criteriaQuery.where(criteriaBuilder.in(root.get("tagsId")).value(ids)).getRestriction();
            }
        });
        return categoryTags;
    }




    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
