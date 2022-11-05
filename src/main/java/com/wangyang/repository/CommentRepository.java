package com.wangyang.repository;

import com.wangyang.pojo.entity.Comment;
import com.wangyang.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentRepository extends BaseRepository<Comment,Integer> {
//    List<Comment> deleteByArticleId(int id);
}
