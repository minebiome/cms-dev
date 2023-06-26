package com.wangyang.weixin.entity;

import com.wangyang.pojo.entity.base.BaseEntity;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Time;
import java.util.Date;

@Entity
@Data
public class MsgReplyRule extends BaseEntity {

//    private static final long serialVersionUID = 1L;
//    @TableId(type = IdType.AUTO)
//    private Long ruleId;
//    private String appid;
//    @NotEmpty(message = "规则名称不得为空")
    private String ruleName;
//    @NotEmpty(message = "匹配关键词不得为空")
    private String matchValue;
    private boolean exactMatch;
    private String replyType;
//    @NotEmpty(message = "回复内容不得为空")
    private String replyContent;
    @Column(name = "status_")
    private boolean status;
    @Column(name = "desc_", columnDefinition = "longtext not null")
    private String desc;
    private Time effectTimeStart;
    private Time effectTimeEnd;
    private int priority;
//    private Date updateTime;

//    @Override
//    public String toString() {
//        return Json.toJsonString(this);
//    }


}
