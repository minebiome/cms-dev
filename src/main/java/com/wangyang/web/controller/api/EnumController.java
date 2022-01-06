package com.wangyang.web.controller.api;

import com.wangyang.common.exception.CmsException;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.pojo.enums.TemplateType;
import com.wangyang.pojo.enums.ValueEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/enum")
public class EnumController {

    @GetMapping
    public ValueEnum[] listName(@RequestParam String name){

        if(name.equals("TemplateType")){
            return TemplateType.values();
        }
        throw new ObjectException("enum 不存在！");
    }
}
