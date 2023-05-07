package com.wangyang.pojo.dto;

import com.wangyang.pojo.entity.Article;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.Set;

@Data
public class ArticlePageCondition {
    Page<Article> articles;
    Set<Integer> ids;
    Set<String> sortStr;
    String order;
    Integer page;
    Integer size;
    Integer totalPage;
}
