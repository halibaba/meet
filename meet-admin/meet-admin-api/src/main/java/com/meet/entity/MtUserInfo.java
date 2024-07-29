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
 * <p>
 * 用户基本信息表
 * </p>
 *
 * @author huangjiayi
 * @since 2022-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("MT_USER_INFO")
@ApiModel(value="MtUserInfo对象", description="用户基本信息表")
public class MtUserInfo extends BaseModel {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户姓名")
    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "性别")
    @TableField("SEX")
    private Integer sex;

    @ApiModelProperty(value = "电话")
    @TableField("PHONE")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @TableField("EMAIL")
    private String email;

    @ApiModelProperty(value = "出生日期")
    @TableField("BIRTHDAY")
    private LocalDateTime birthday;

    @ApiModelProperty(value = "学历")
    @TableField("EDUCATION")
    private Integer education;

    @ApiModelProperty(value = "身份证号")
    @TableField("IDENTITY")
    private String identity;

    @ApiModelProperty(value = "用户地址")
    @TableField("ADDRES")
    private String addres;

    @ApiModelProperty(value = "状态")
    @TableField("STATUS")
    private Integer status;
}
