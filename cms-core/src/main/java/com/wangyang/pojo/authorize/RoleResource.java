package com.wangyang.pojo.authorize;

import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@Entity(name = "t_role_resources")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class RoleResource extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    private Integer resourceId;
    private Integer roleId;
}
