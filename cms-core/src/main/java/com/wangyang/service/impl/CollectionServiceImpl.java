package com.wangyang.service.impl;

import com.wangyang.pojo.entity.Collection;
import com.wangyang.common.enums.CrudType;
import com.wangyang.pojo.vo.CollectionVO;
import com.wangyang.repository.CollectionRepository;
import com.wangyang.service.ICollectionService;
import com.wangyang.service.ITaskService;
import com.wangyang.service.base.AbstractBaseCategoryServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionServiceImpl extends AbstractBaseCategoryServiceImpl<Collection,Collection, CollectionVO> implements ICollectionService {

    CollectionRepository collectionRepository;
    private ITaskService taskService;
    public CollectionServiceImpl(CollectionRepository collectionRepository, ITaskService taskService) {
        super(collectionRepository);
        this.collectionRepository=collectionRepository;
        this.taskService = taskService;
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
