package com.meet.gateway.config;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mt
 * @date 2020/3/15 13:49
 */
//@Configuration
public class GateWayConfig {
    /**
     * 配置一个id为path_route_test的路由规则，
     * 路由所有的访问请求
     * @param routeLocatorBuilder
     * @return
     */
    @SuppressWarnings("JavaDoc")
//    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("path_route_test",
                r -> r.path("/**")
                        .uri("http://localhost:3000")).build();
        return routes.build();
    }
}