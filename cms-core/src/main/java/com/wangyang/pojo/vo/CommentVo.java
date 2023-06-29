package com.wangyang.pojo.vo;

import com.wangyang.common.pojo.BaseVo;
import com.wangyang.pojo.authorize.User;
import com.wangyang.pojo.entity.Comment;
import lombok.Data;

import java.util.Date;

@Data
public class CommentVo extends BaseVo<Comment> {
    private Date createDate;
    private Date updateDate;
    private Integer id;
    private Integer userId;
    private Integer articleId;
//    private String username;
//    private String email;
    private String content;
    private User user;
    private User replyUser;
    private String originalContent;
    private String formatContent;
    public CommentVo() {
    }

    public CommentVo(String content) {
        this.content = content;
    }
}
