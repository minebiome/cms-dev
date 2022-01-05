package com.wangyang.util;

import com.wangyang.pojo.authorize.User;

import javax.servlet.http.HttpServletRequest;

public class AuthorizationUtil {

    public static Integer getUserId(HttpServletRequest request){
        Object obj = request.getAttribute("user");
        if(obj!=null){
            return ((User)obj).getId();
        }
        throw new AuthorizationException("用户id不存在！");

    }
    public static User getUser(HttpServletRequest request){
        Object obj = request.getAttribute("user");
        if(obj!=null){
            return ((User)obj);
        }
        return null;
    }
}
