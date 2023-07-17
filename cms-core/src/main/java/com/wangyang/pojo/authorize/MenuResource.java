package com.wangyang.pojo.authorize;

import com.wangyang.common.pojo.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

/**
 * 惨淡资源
 */
@Entity
@Getter
@Setter
@Cacheable
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class MenuResource extends BaseEntity {

    /*
    菜单name
     */
    private String name;

    /*
    菜单名称
     */
    private String title;

    private String component;

    private String icon;

    /*
    菜单url
     */
    private String url;

}
