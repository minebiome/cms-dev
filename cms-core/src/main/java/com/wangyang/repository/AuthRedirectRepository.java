package com.wangyang.repository;

import com.wangyang.common.repository.BaseRepository;
import com.wangyang.pojo.authorize.Resource;
import com.wangyang.pojo.entity.AuthRedirect;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

public interface AuthRedirectRepository extends BaseRepository<AuthRedirect,Integer> {
    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<AuthRedirect> findAll();
}
