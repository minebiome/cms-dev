package com.wangyang.service.impl;

import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.Collection;
import com.wangyang.pojo.enums.CrudType;
import com.wangyang.pojo.vo.CategoryVO;
import com.wangyang.pojo.vo.CollectionVO;
import com.wangyang.repository.CollectionRepository;
import com.wangyang.repository.base.BaseCategoryRepository;
import com.wangyang.service.ICategoryService;
import com.wangyang.service.ICollectionService;
import com.wangyang.service.base.AbstractBaseCategoryServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionServiceImpl extends AbstractBaseCategoryServiceImpl<Collection,Collection, CollectionVO> implements ICollectionService {

    CollectionRepository collectionRepository;
    public CollectionServiceImpl(CollectionRepository collectionRepository) {
        super(collectionRepository);
        this.collectionRepository=collectionRepository;
    }

    @Override
    public List<CollectionVO> listTree() {
        List<Collection> collections = listAll();
        List<CollectionVO> collectionVOS = super.convertToListVo(collections);
        return super.listWithTree(collectionVOS);
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
