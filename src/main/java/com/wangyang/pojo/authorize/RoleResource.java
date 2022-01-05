package com.wangyang.pojo.authorize;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@Entity(name = "t_role_resources")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class RoleResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Integer resourceId;
    private Integer roleId;
}