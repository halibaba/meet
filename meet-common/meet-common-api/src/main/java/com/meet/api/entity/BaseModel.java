package com.meet.api.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.meet.api.enums.EnabledEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description 通用字段
 * @Author HuangJiaYi
 * @Date 2022-11-26
 */
@Data
public class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(updateStrategy = FieldStrategy.IGNORED, insertStrategy = FieldStrategy.IGNORED, whereStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.VARCHAR)
    @ApiModelProperty(hidden = true)
    private String description;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(hidden = true)
    private Integer isDeleted = EnabledEnum.N.getValue();

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(hidden = true)
    private LocalDateTime createDate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(hidden = true)
    private LocalDateTime lastUpdateDate;

    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    @ApiModelProperty(hidden = true)
    private String createBy;

    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    @ApiModelProperty(hidden = true)
    private String lastUpdateBy;


}
