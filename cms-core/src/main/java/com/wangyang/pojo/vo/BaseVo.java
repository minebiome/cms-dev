package com.wangyang.pojo.vo;

import com.wangyang.pojo.entity.base.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class BaseVo<T> {
    private Integer id;
    private Integer parentId;
    private Integer order;
    private List<T> children;
}
