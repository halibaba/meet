package com.meet.elasticesearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: meet-boot
 * @ClassName ElasticSearchApplication
 * @description:
 * @author: MT
 * @create: 2023-07-20 11:30
 **/
@SpringBootApplication
@EnableTransactionManagement
public class ElasticSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchApplication.class, args);
    }
}