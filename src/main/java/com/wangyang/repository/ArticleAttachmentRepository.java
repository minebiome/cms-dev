package com.wangyang.repository;

import com.wangyang.pojo.entity.ArticleAttachment;
import com.wangyang.repository.base.BaseRepository;

import java.util.List;

public interface ArticleAttachmentRepository extends BaseRepository<ArticleAttachment,Integer> {

    List<ArticleAttachment> findByTemplateId(Integer id);
}
