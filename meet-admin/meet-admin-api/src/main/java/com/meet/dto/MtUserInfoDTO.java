package com.meet.dto;

import com.meet.entity.MtUserInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户扩展信息入参。username 只用于按 acl_user 查询，不落在扩展表。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MtUserInfoDTO extends MtUserInfo {

    @ApiModelProperty(value = "用户名")
    private String username;
}
