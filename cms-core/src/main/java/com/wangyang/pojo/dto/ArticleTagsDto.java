package com.wangyang.pojo.dto;

import lombok.Data;

@Data
public class ArticleTagsDto {
    private String tag;
    private String key;


    public ArticleTagsDto(String tag, String key) {
        this.tag = tag;
        this.key = key;
    }
}
