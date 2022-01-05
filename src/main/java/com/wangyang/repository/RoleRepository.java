package com.wangyang.repository;


import com.wangyang.pojo.authorize.Role;
import com.wangyang.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

/**
 * @author wangyang
 * @date 2021/5/5
 */


public interface RoleRepository extends BaseRepository<Role,Integer> {
    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<Role> findAll();

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    Optional<Role> findById(Integer integer);
}
