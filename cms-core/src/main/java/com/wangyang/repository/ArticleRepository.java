package com.wangyang.repository;

import com.wangyang.pojo.entity.Article;
import com.wangyang.repository.base.ContentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends ContentRepository<Article> {

    /**
     * update article likes by article id
     * @param id
     * @return
     */
    @Query("update Article a set a.likes = a.likes+1 where a.id = :aid")
    @Modifying
    int updateLikes(@Param("aid") int id);

    @Query(value = "select o.id from Article o where status='PUBLISH'")
    List<Integer> findAllId();

    @Query(value = "select o.likes from Article o where id = ?1 ")
    Integer getLikesNumber(int id);

    @Query("update Article a set a.visits = a.visits+1 where a.id = :aid")
    @Modifying
    int updateVisits(@Param("aid") int id);



    @Query(value = "select o.visits from Article o where id = ?1 ")
    Integer getVisitsNumber(int id);

    @Query("update Article a set a.commentNum = a.commentNum+:num where a.id = :aid")
    @Modifying
    int updateCommentNum(@Param("aid") int id, int num);

    @Query(value = "select o.commentNum from Article o where id = ?1 ")
    Integer getCommentNum(int id);

//    @Query("select o from Article o where o.id in (select a.articleId from ArticleCategory a where a.categoryId=?1)")
//    List<Article> findByCategoryId(int id);

    Article findByIdAndUserId(int id, int userId);

    Article findByViewName(String viewName);

    Article findTopByOrderByIdDesc();

    int countBycategoryId(int id);
}
