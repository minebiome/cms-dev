package com.wangyang.pojo.authorize;

import lombok.Data;

import java.util.Set;

@Data
public class UserDetailDTO extends User {
    Set<Role> roles;
//    Set<String> rolesStr;
}
