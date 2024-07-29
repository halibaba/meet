package com.meet.message;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @program: meet-boot
 * @ClassName MeetMessageApplication
 * @description:
 * @author: MT
 * @create: 2022-12-21 09:35
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.meet"})
@MapperScan({"com.meet.message.mapper"})
@Slf4j
public class MessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
        log.info("=========================启动成功===========================");
    }
}