package com.wangyang.pojo.vo;

import lombok.Data;

@Data
public class CollectionVO extends BaseVo<CollectionVO> {
    private String name;
    private String key;
    private String parentKey;
    private String version;
}
