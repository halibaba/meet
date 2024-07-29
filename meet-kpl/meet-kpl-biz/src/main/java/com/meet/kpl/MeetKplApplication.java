package com.meet.kpl;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @program: meet-boot
 * @ClassName MeetKplApplication
 * @description:
 * @author: MT
 * @create: 2022-12-02 13:50
 **/

@SpringBootApplication
@EnableDiscoveryClient
//@EnableFeignClients(basePackages = {"com.meet"})
//@MapperScan({"com.meet.kpl.mapper"})
@Slf4j
public class MeetKplApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetKplApplication.class, args);
        log.info("=================启动成功===================");
    }

}