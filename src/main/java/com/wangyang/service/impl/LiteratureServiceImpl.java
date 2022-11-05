package com.wangyang.service.impl;

import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.Literature;
import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.pojo.enums.CrudType;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.pojo.vo.CategoryVO;
import com.wangyang.repository.LiteratureRepository;
import com.wangyang.repository.base.BaseRepository;
import com.wangyang.service.ICategoryService;
import com.wangyang.service.ILiteratureService;
import com.wangyang.service.base.AbstractCrudService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class LiteratureServiceImpl
        extends AbstractCrudService<Literature, BaseEntity, BaseVo,Integer> implements ILiteratureService {

    private LiteratureRepository literatureRepository;
    public LiteratureServiceImpl(LiteratureRepository literatureRepository) {
        super(literatureRepository);
        this.literatureRepository = literatureRepository;
    }

    @Override
    public List<Literature> listByKeys(Set<String> literatureStrIds) {
//        List<Literature> literatures = new ArrayList<>();

        List<Literature> literatures = literatureRepository.findAll(new Specification<Literature>() {
            @Override
            public Predicate toPredicate(Root<Literature> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(root.get("key").in(literatureStrIds)).getRestriction();
            }
        });


        return literatures;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
