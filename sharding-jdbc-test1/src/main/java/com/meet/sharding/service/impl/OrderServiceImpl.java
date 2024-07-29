package com.meet.sharding.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meet.entity.MtUserInfo;
import com.meet.sharding.entity.Order;
import com.meet.sharding.mapper.OrderMapper;
import com.meet.sharding.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * @program: meet-boot
 * @ClassName OrderServiceImpl
 * @description:
 * @author: MT
 * @create: 2023-07-19 17:08
 **/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    public Boolean insert(Order order) {
        return this.save(order);
    }
}