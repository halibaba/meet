package com.meet.dto;

import lombok.Data;

import java.util.List;

/**
 * 登录校验所需的用户信息，由 admin 查询后交给认证服务。
 */
@Data
public class LoginUserDTO {

    private String username;

    private String password;

    private String nickName;

    private String salt;

    private String token;

    private List<String> permissionValueList;
}
