package com.wangyang.repository;

import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AttachmentRepository extends JpaRepository<Attachment,Integer>
        , JpaSpecificationExecutor<Attachment> {

}
