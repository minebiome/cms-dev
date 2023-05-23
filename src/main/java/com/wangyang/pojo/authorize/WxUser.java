package com.wangyang.pojo.authorize;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@Entity
@DiscriminatorValue(value = "2")
@Data
public class WxUser extends BaseAuthorize{
    private String openId;

}

//@Entity
//@DiscriminatorValue(value = "1")
//@Data
//public class APIUser extends BaseAuthorize{
//    private String authorize;
//}
