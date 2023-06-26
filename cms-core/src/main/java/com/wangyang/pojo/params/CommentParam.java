package com.wangyang.pojo.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class CommentParam {


    private Integer userId;
    @NotNull(message = "文章Id不能为空!")
    private Integer articleId;
//    private CommentType commentType;
//    @NotBlank(message = "用户昵称不能为空")
    private String username;
//    @NotBlank(message = "用户电子邮件不能为空")
    private String email;

    private String content;
    private Integer parentId;
    @NotBlank(message = "评论内容不能为空!")
    private String originalContent;

}
