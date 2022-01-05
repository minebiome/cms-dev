package com.wangyang.repository;


import com.wangyang.pojo.authorize.Resource;
import com.wangyang.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

/**
 * @author wangyang
 * @date 2021/5/5
 */
public interface ResourceRepository extends BaseRepository<Resource,Integer> {
    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<Resource> findAll();

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    Optional<Resource> findById(Integer integer);

    //    @Override
//    default  List<Resource> findAll(){
//        List<Resource> resources = CacheStore.getList("Resource", Resource.class);
//        if(resources==null){
//            super.findAll();
//            resources = super.
//            CacheStore.save("Resource",resources);
//        }
//        return resources;
//    }

//    default  List<Resource> save(){
//        List<Resource> resources = CacheStore.getList("Resource", Resource.class);
//        if(resources==null){
//            resources = this.findAll();
//            CacheStore.save("Resource",resources);
//        }
//        return resources;
//    }

}
