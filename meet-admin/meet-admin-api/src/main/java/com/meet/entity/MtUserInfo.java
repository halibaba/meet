package com.meet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meet.api.entity.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户扩展信息，通过 user_id 关联 acl_user.id。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mt_user_info")
@ApiModel(value = "MtUserInfo对象", description = "用户扩展信息表")
public class MtUserInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联acl_user.id")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "用户姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "性别")
    @TableField("sex")
    private Integer sex;

    @ApiModelProperty(value = "电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "出生日期")
    @TableField("birthday")
    private LocalDateTime birthday;

    @ApiModelProperty(value = "学历")
    @TableField("education")
    private Integer education;

    @ApiModelProperty(value = "身份证号")
    @TableField("identity")
    private String identity;

    @ApiModelProperty(value = "用户地址")
    @TableField("addres")
    private String addres;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private Integer status;
}
