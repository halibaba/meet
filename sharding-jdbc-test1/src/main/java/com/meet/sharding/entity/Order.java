package com.meet.sharding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: meet-boot
 * @ClassName Order
 * @description:
 * @author: MT
 * @create: 2023-07-19 16:59
 **/
@Data
@TableName("`order`")
public class Order implements Serializable {

    private static final long serialVersionUID = 661434701950670670L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("order_id")
    private Long orderId;

    @TableField("user_id")
    private Integer userId;

    @TableField("address_id")
    private Long addressId;

    @TableField("status")
    private String status;
}
