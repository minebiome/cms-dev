package com.wangyang.pojo.entity;

import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.pojo.enums.TaskStatus;
import com.wangyang.pojo.enums.TaskType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Table(name = "t_task")
@Entity
public class Task extends BaseEntity {
    private String name;
    private String enName;
    private TaskType taskType;
    public TaskStatus status=TaskStatus.FINISH;
}
