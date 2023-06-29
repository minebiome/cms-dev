package com.wangyang.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

@Component("JSONUtils")
public class JSONUtils {
    public static String json(Object o){
        return JSON.toJSON(o).toString();
    }
}
