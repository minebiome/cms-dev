package com.wangyang.pojo.authorize;

import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "t_base_authorize")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")
@Data
public class BaseAuthorize extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    protected int id;
    private String username;
    private String nickname;
    private String avatar;
    private String email;
    private String phone;


}
