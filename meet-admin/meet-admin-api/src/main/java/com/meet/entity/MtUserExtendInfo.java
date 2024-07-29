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

/**
 * <p>
 * 用户扩展信息表
 * </p>
 *
 * @author huangjiayi
 * @since 2022-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("MT_USER_EXTEND_INFO")
@ApiModel(value="MtUserExtendInfo对象", description="用户扩展信息表")
public class MtUserExtendInfo extends BaseModel {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    @TableField("USER_INFO_ID")
    private Integer userInfoId;

    @ApiModelProperty(value = "用户名")
    @TableField("USERNAME")
    private String username;

    @ApiModelProperty(value = "密码")
    @TableField("PASSWORK")
    private String passwork;

    @ApiModelProperty(value = "昵称")
    @TableField("NICKNAME")
    private String nickname;

    @ApiModelProperty(value = "头像")
    @TableField("AVATAR")
    private String avatar;

    @ApiModelProperty(value = "金币数量")
    @TableField("COUNT")
    private Integer count;

    @ApiModelProperty(value = "状态")
    @TableField("STATUS")
    private Integer status;

    @ApiModelProperty(value = "用户类型")
    @TableField("TYPE")
    private Integer type;


}
