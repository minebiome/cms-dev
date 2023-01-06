package com.wangyang.pojo.dto;

import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.vo.ArticleVO;
import com.wangyang.pojo.vo.CategoryVO;
import lombok.Data;

import java.util.List;

@Data
public class CategoryArticleList {
    private CategoryVO category;
    private List<ArticleVO> articleVOS;
}
