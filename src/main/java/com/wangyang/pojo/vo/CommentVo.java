package com.wangyang.pojo.vo;

import com.wangyang.pojo.authorize.User;
import com.wangyang.pojo.dto.CommentDto;
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

    public CommentVo() {
    }

    public CommentVo(String content) {
        this.content = content;
    }
}
