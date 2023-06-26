package com.wangyang.repository.authorize;


import com.wangyang.pojo.authorize.RoleResource;
import com.wangyang.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface RoleResourceRepository extends BaseRepository<RoleResource,Integer> {

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<RoleResource> findAll();

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    Optional<RoleResource> findById(Integer integer);
//    default  List<RoleResource> listAll(){
//        List<RoleResource> roleResource = CacheStore.getList("RoleResource", RoleResource.class);
//        if(roleResource==null){
//            roleResource = this.findAll();
//            CacheStore.save("RoleResource",roleResource);
//        }
//        return roleResource;
//    }
}
