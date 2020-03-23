package com.wangyang.cms.core.jms.consumer;

import com.wangyang.cms.core.jms.producer.DestinationConst;
import com.wangyang.cms.pojo.vo.ArticleDetailVO;
import com.wangyang.cms.service.IHtmlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ArticleConsumerServiceImpl {

    @Autowired
    IHtmlService htmlService;

    @JmsListener(destination = DestinationConst.ARTICLE_HTML_STRING)
    public void receiveArticleDetailVO(ArticleDetailVO articleVO){
        htmlService.conventHtml(articleVO);
    }

}
