package com.wangyang.pojo.authorize;

import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * @author wangyang
 * @date 2021/5/5
 */


@Entity(name = "t_role")
@Data
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Role extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    private String name;
    private String enName;

    public Role(String enName) {
        this.enName = enName;
    }

    public Role() {
    }
    //    @ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.LAZY)
//    @JoinTable(name = "t_user_role",joinColumns = @JoinColumn(name = "roleId"),
//    inverseJoinColumns = @JoinColumn(name = "userId"))
//    @JsonBackReference
//    private Set<User> users = new HashSet<>();
//
//    @ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.LAZY)
//    @JoinTable(name = "t_role_resource",joinColumns = @JoinColumn(name = "roleId"),
//    inverseJoinColumns = @JoinColumn(name = "resourceId"))
//    private Set<Resource> resources = new HashSet<>();

}
