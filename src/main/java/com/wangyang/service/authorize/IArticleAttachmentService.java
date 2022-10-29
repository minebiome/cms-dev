package com.wangyang.service.authorize;

import com.wangyang.pojo.authorize.Role;
import com.wangyang.pojo.entity.ArticleAttachment;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.service.base.ICrudService;

import java.util.List;

public interface IArticleAttachmentService extends ICrudService<ArticleAttachment,ArticleAttachment, BaseVo, Integer> {

    List<ArticleAttachment> findByTemplateId(Integer id);
}
