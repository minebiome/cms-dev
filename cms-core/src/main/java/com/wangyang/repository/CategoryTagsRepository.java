package com.wangyang.repository;

import com.wangyang.pojo.entity.ArticleTags;
import com.wangyang.pojo.entity.CategoryTags;
import com.wangyang.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CategoryTagsRepository  extends BaseRepository<CategoryTags,Integer> {
    List<CategoryTags> deleteByCategoryId(int id);
    List<CategoryTags> findByCategoryId(int articleId);
    List<CategoryTags> findByTagsId(int tagsId);
}
