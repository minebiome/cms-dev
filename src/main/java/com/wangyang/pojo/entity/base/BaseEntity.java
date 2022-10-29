package com.wangyang.pojo.entity.base;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "int default 0")
    private Integer parentId=0;
    @Column(name = "order_",columnDefinition = "int default 0")
    private Integer order;


    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate=new Date();

    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate=new Date();

}
