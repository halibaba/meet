package com.meet.sharding.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meet.sharding.entity.Order;

/**
 * @program: meet-boot
 * @interfaceName OrderService
 * @description:
 * @author: MT
 * @create: 2023-07-19 17:07
 **/
public interface OrderService extends IService<Order> {

    Boolean insert(Order order);
}