package com.wangyang.pojo.authorize;

import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "t_base_authorize")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")
@Data
public class BaseAuthorize extends BaseEntity {

    private String username;

    private String nickname;

    private String avatar;

    private String email;

    private String phone;

    /*
    用户来源：1、系统添加 2、微信登录用户
     */
    @Column(name = "source_")
    private String source;

}
