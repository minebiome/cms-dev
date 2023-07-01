package com.wangyang.weixin.service.impl;

import lombok.Data;

@Data
public class WxSubscribeMessageParam {
    private String title;
    private String color;
    private String content;
}
