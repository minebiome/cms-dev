package com.wangyang.pojo.authorize;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@Entity
@DiscriminatorValue(value = "2")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxUser extends BaseAuthorize{

    private String openId;

    /*
    该字段废弃。用户会有多个角色
    使用父类的source字段取代
     */
    // @Deprecated
    private String roleEn;

    private Integer gender;

}

//@Entity
//@DiscriminatorValue(value = "1")
//@Data
//public class APIUser extends BaseAuthorize{
//    private String authorize;
//}
