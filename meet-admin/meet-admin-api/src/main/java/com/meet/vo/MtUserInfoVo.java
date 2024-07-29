package com.meet.vo;
import com.meet.entity.MtUserInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: mt
 * @description: TODO
 * @date: 2022/11/29 10:23 上午
 */
@Data
public class MtUserInfoVo extends MtUserInfo {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String passwork;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "金币数量")
    private Integer count;

    @ApiModelProperty(value = "用户类型")
    private Integer type;
}
