package com.wangyang.pojo.entity;

import com.wangyang.pojo.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "options")
@Data
public class Option extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    /**
     * option key
     */
    @Column(name = "option_key", columnDefinition = "varchar(100) not null")
    private String key;
    /**
     * option value
     */
    @Column(name = "option_value", columnDefinition = "varchar(1023) not null")
    private String value;

    private String name;
    private Integer groupId;


    public Option(){}

    public Option(String key,String value,String name,Integer groupId){
        this.key=key;
        this.value=value;
        this.name=name;
        this.groupId=groupId;
    }


}
