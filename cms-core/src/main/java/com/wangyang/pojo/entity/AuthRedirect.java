package com.wangyang.pojo.entity;

import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class AuthRedirect extends BaseEntity {
    private String currentUrl;
    private String authUrl;
    private String loginRedirect;
    private String subscribeRedirect;
    private String loginAuthRedirect;
    private String loginPage;

}
