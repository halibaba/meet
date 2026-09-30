package com.meet.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户扩展信息。账号、密码、昵称、头像在 acl_user。
 */
@Data
public class MtUserInfoVo {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "关联acl_user.id")
    private String userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "出生日期")
    private LocalDateTime birthday;

    @ApiModelProperty(value = "学历")
    private Integer education;

    @ApiModelProperty(value = "身份证号")
    private String identity;

    @ApiModelProperty(value = "用户地址")
    private String addres;

    @ApiModelProperty(value = "状态")
    private Integer status;
}
