package com.meet.sharding.controller;

import com.meet.sharding.entity.Order;
import com.meet.sharding.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: meet-boot
 * @ClassName OrderController
 * @description:
 * @author: MT
 * @create: 2023-07-19 17:08
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("add")
    public Boolean add(@RequestBody Order order){
        return orderService.insert(order);
    }
}