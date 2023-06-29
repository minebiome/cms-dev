package com.wangyang.repository.authorize;

import com.wangyang.pojo.entity.ArticleAttachment;
import com.wangyang.common.repository.BaseRepository;

import java.util.List;

public interface ArticleAttachmentRepository extends BaseRepository<ArticleAttachment,Integer> {

    List<ArticleAttachment> findByTemplateId(Integer id);
}
