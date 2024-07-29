package com.meet.sharding;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

/**
 * @program: meet-boot
 * @ClassName ShardingApplication
 * @description:
 * @author: MT
 * @create: 2023-07-19 15:53
 **/
@SpringBootApplication
@Slf4j
public class ShardingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingApplication.class, args);
        log.info("=========================启动成功===========================");
    }
}