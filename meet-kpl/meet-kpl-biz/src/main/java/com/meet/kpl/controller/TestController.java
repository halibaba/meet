package com.meet.kpl.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meet.pub.entity.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

/**
 * @program: meet-boot
 * @ClassName TestController
 * @description:
 * @author: MT
 * @create: 2022-12-02 15:28
 **/

@RestController
@RequestMapping("/mt-user-info")
public class TestController {

    @GetMapping("query_page")
    public ZonedDateTime queryPage(){
        ZonedDateTime now = ZonedDateTime.now();
        return now;
    }

}