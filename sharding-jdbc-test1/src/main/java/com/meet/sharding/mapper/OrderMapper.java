package com.meet.sharding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meet.sharding.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: meet-boot
 * @ClassName OrderMapper
 * @description:
 * @author: MT
 * @create: 2023-07-19 17:19
 **/
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}