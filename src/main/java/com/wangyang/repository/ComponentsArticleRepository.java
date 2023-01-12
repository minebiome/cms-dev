package com.wangyang.repository;

import com.wangyang.pojo.entity.ComponentsArticle;
import com.wangyang.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComponentsArticleRepository extends BaseRepository< ComponentsArticle,Integer> {

    List<ComponentsArticle> findByComponentId(Integer componentId);

    ComponentsArticle findByArticleIdAndComponentId(int articleId, int componentId);
}
