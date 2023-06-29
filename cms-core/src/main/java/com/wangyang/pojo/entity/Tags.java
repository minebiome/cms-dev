package com.wangyang.pojo.entity;

import com.wangyang.common.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Data
public class Tags extends BaseEntity  implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    private String name;
    private String description;
    private String slugName;
    private String enName;

    public Tags(){}
    public Tags(String name,String slugName){
        this.name = name;
        this.slugName = slugName;
    }

    public Tags(String name){
        this.name = name;
    }
}
