package com.wangyang.pojo.authorize;

import com.wangyang.pojo.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * @author wangyang
 * @date 2021/5/5
 */
@Entity(name = "t_resource")
@Getter
@Setter
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Resource extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    private String name;
    private String url;
    private String method;

//    @ManyToMany(cascade = {CascadeType.MERGE})
//    @JoinTable(name = "t_role_resource",joinColumns = @JoinColumn(name = "resourceId"),
//    inverseJoinColumns = @JoinColumn(name = "roleId"))
//    private Set<Role> roles = new HashSet<>();
}
