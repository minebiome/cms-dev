package com.wangyang.service;

import com.wangyang.pojo.authorize.Role;
import com.wangyang.pojo.entity.ArticleAttachment;
import com.wangyang.service.base.ICrudService;

import java.util.List;

public interface IArticleAttachmentService extends ICrudService<ArticleAttachment, Integer> {

    List<ArticleAttachment> findByTemplateId(Integer id);
}
