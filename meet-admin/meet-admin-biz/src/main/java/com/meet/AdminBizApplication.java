package com.meet;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan({"com.meet.admin.mapper"})
@Slf4j
public class AdminBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminBizApplication.class, args);
        log.info("=================启动成功===================");
    }
}
