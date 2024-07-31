package com.meet.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: meet-boot
 * @ClassName GatewayConfig
 * @description: yml中的配置可以改成java代码配置
 * @author: MT
 * @create: 2023-05-23 13:43
 **/
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("admin_routh", r -> r
                        .path("/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://meet-admin-biz")
                )
                .build();
    }
}