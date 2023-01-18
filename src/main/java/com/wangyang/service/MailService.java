package com.wangyang.service;

import com.wangyang.pojo.authorize.BaseAuthorize;
import com.wangyang.pojo.authorize.User;
import com.wangyang.pojo.entity.*;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import java.util.List;

/**
 * @author Likaifeng
 * @date 2021/6/22
 * 简单文本邮件
 */
public interface MailService {

    /**
     * 简单文本邮件
     * @param to      接收者邮件
     * @param subject 邮件主题
     * @param contnet 邮件内容
     */
    void sendSimpleMail(String to, String subject, String contnet);

    /**
     * HTML 文本邮件
     * @param to      接收者邮件
     * @param subject 邮件主题
     * @param contnet HTML内容
     */
    void sendHtmlMail(String to, String subject, String contnet) throws MessagingException;

    /**
     * 附件邮件
     * @param to 接收者邮件
     * @param subject 邮件主题
     * @param contnet HTML内容
     * @param filePath 附件路径
     */
    void sendAttachmentsMail(String to, String subject, String contnet, String filePath) throws MessagingException;

    /**
     * 图片邮件
     * @param to 接收者邮件
     * @param subject 邮件主题
     * @param contnet HTML内容
     * @param rscPath 图片路径
     * @param rscId 图片ID
     */
    void sendInlinkResourceMail(String to, String subject, String contnet, String rscPath, String rscId) throws MessagingException;

    @Async("taskExecutor")
    void sendEmail(User currentUser, Comment comment, Article article);

    void sendEmail(Customer customer);

    void sendEmail(Subscribe subscribe);

    List<BaseAuthorize> sendEmail(Mail mailInput);
}
