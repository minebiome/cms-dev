package com.wangyang.weixin.entity;

import com.wangyang.pojo.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Data
public class WxQrCode extends BaseEntity {
    /**
     * ID
     */
//    private Long id;
//    private String appid;
    /**
     * 二维码类型
     */
    private Boolean isTemp;
    /**
     * 场景值ID
     */
    private String sceneStr;
    /**
     * 二维码ticket
     */
    private String ticket;
    /**
     * 二维码图片解析后的地址
     */
    private String url;
    /**
     * 该二维码失效时间
     */
    private Date expireTime;

}
