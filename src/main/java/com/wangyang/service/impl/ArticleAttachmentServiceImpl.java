package com.wangyang.service.impl;

import com.wangyang.pojo.authorize.Resource;
import com.wangyang.pojo.entity.ArticleAttachment;
import com.wangyang.repository.ArticleAttachmentRepository;
import com.wangyang.repository.base.BaseRepository;
import com.wangyang.service.IArticleAttachmentService;
import com.wangyang.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleAttachmentServiceImpl extends AbstractCrudService<ArticleAttachment,Integer>
        implements IArticleAttachmentService {

    private ArticleAttachmentRepository articleAttachmentRepository;
    public ArticleAttachmentServiceImpl(ArticleAttachmentRepository articleAttachmentRepository) {
        super(articleAttachmentRepository);
        this.articleAttachmentRepository=articleAttachmentRepository;
    }

    @Override
    public List<ArticleAttachment> findByTemplateId(Integer id) {
        return articleAttachmentRepository.findByTemplateId(id);
    }
}
