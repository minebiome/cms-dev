package com.wangyang.pojo.entity;

import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

@Data
@Entity
@Getter
@Setter
@Cacheable
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class AuthRedirect extends BaseEntity {
    private String title;
    private String description;
    private String currentUrl;
    private String authUrl;
    private String loginRedirect;
    private String subscribeRedirect;
    private String loginAuthRedirect;
    private String viewName;
    private String path;

    private String templateName;
    private String loginPage;

}
