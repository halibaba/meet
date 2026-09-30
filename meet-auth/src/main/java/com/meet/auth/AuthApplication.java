package com.meet.auth;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.meet.feign")
@SpringBootApplication
//使用注解开启权限校验时需要使用该注解
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
