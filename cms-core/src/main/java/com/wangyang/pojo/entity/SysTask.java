package com.wangyang.pojo.entity;

import com.wangyang.common.pojo.BaseEntity;
import com.wangyang.pojo.enums.ScheduleStatus;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity(name = "t_sys_task")
public class SysTask extends BaseEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    private String jobName;
    private String description;
    private String cornExpression;
    private String beanClass;
    private String jobGroup;
    private ScheduleStatus scheduleStatus;
    private String methodName;
    private String args;
    private Integer userId;

}
