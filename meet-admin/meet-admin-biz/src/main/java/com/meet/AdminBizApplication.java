package com.meet;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.meet"})
@MapperScan({"com.meet.admin.mapper"})
@Slf4j
public class AdminBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminBizApplication.class, args);
        log.info("=================启动成功===================");
    }
}
