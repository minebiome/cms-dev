package com.wangyang.pojo.dto;

import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.vo.CategoryVO;
import lombok.Data;

import java.util.List;

@Data
public class CategoryChild {
    private CategoryVO category;
    private List<CategoryVO> categoryVOS;
}
