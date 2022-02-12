package com.wangyang.repository.authorize;


import com.wangyang.pojo.authorize.UserRole;
import com.wangyang.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends BaseRepository<UserRole,Integer> {

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<UserRole> findAll();

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    Optional<UserRole> findById(Integer integer);
//    default  List<UserRole> listAll(){
//        List<UserRole> userRoles = CacheStore.getList("UserRole", UserRole.class);
//        if(userRoles==null){
//            userRoles = this.findAll();
//            CacheStore.save("UserRole",userRoles);
//        }
//        return userRoles;
//    }
}
