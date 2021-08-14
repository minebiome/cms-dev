package com.wangyang.authorize.dto;

import lombok.Data;

/**
 * @author wangyang
 * @date 2020/12/24
 */
@Data
public class CmsToken {
    private String token;
    private long exp;

    public CmsToken(String token, long exp) {
        this.token = token;
        this.exp = exp;
    }
}
