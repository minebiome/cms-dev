package com.wangyang.pojo.authorize;

import lombok.Data;

import java.util.Set;

@Data
public class ApiUserDetailDTO extends APIUser {
    Set<Role> roles;
}
