package com.wangyang.cms.service.impl;

import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.handle.FileHandler;
import com.wangyang.cms.handle.FileHandlers;
import com.wangyang.cms.pojo.entity.Attachment;
import com.wangyang.cms.pojo.enums.AttachmentType;
import com.wangyang.cms.pojo.enums.PropertyEnum;
import com.wangyang.cms.pojo.support.UploadResult;
import com.wangyang.cms.repository.AttachmentRepository;
import com.wangyang.cms.service.IAttachmentService;
import com.wangyang.cms.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttachmentServiceImpl implements IAttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    FileHandlers fileHandlers;

    @Autowired
    IOptionService optionService;


    @Override
    public Attachment add(Attachment attachment) {
        return attachmentRepository.save(attachment);
    }

    @Override
    public Attachment upload(MultipartFile file) {
        //TODO AttachmentType.LOCAL from databases
        UploadResult uploadResult = fileHandlers.upload(file, getAttachmentType());
        Attachment attachment = new Attachment();
        ///upload/2020/2/Screenshot from 2020-02-28 15-43-32-2015c76b-9442-435a-a1b7-ad030548d57f-thumbnail.png
        attachment.setPath(uploadResult.getFilePath());
        ///upload/2020/2/Screenshot from 2020-02-28 15-43-32-2015c76b-9442-435a-a1b7-ad030548d57f.png
        attachment.setFileKey(uploadResult.getKey());
        ///upload/2020/2/Screenshot from 2020-02-28 15-43-32-2015c76b-9442-435a-a1b7-ad030548d57f-thumbnail.png
        attachment.setThumbPath(uploadResult.getThumbPath());
        //image/png
        attachment.setMediaType(uploadResult.getMediaType().toString());
        //png
        attachment.setSuffix(uploadResult.getSuffix());
        attachment.setWidth(uploadResult.getWidth());
        attachment.setHeight(uploadResult.getHeight());
        attachment.setSize(uploadResult.getSize());
        attachment.setType( AttachmentType.LOCAL);
        return attachmentRepository.save(attachment);
    }



    @Override
    public Page<Attachment> list(Pageable pageable){
        return attachmentRepository.findAll(pageable);
    }

    @Override
    public Attachment deleteById(int id){
        Attachment attachment = findById(id);
        attachmentRepository.deleteById(id);
        fileHandlers.delete(attachment);
        return attachment;
    }
    @Override
    public List<Attachment> deleteByIds(Collection<Integer> ids){
        return ids.stream().map(this::deleteById).collect(Collectors.toList());
    }

    @Override
    public Attachment findById(int id){
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if(optionalAttachment==null){
            throw  new ObjectException("Attachment not found!!");
        }
        return optionalAttachment.get();
    }

    public AttachmentType getAttachmentType(){
        String propertyValue = optionService.getPropertyStringValue(PropertyEnum.ATTACHMENT_TYPE);
        if(propertyValue==null){
            return AttachmentType.LOCAL;
        }
        return AttachmentType.valueOf(propertyValue);
    }
}
