package com.wangyang.pojo.authorize;

import com.wangyang.common.enums.Lang;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseAuthorizeDTO {

    private Integer id;
    private Integer parentId;
    private Integer order;


    private Date createDate;

    private Date updateDate;
    private Lang lang;
    private Integer langSource;

    private String username;
    private String nickname;
    private String avatar;
    private String email;
    private String phone;

    private String openId;
    private String roleEn;
    private Integer gender;

    private String source;
}
