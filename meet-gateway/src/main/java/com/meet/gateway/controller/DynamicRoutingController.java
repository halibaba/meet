package com.meet.gateway.controller;

import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Properties;

/**
 * @program: meet-boot
 * @ClassName DynamicRoutingController
 * @description: 除了在yml和java代码中配置路由，也可以通过nacos自动配置，将前端传回来的路由id和地址存储到配置中心，这里我用post请求接收
 * @author: MT
 * @create: 2023-05-23 15:47
 **/
@Configuration
@RestController
@Slf4j
public class DynamicRoutingController {

    private final RouteDefinitionWriter routeDefinitionWriter;
    private final RouteDefinitionLocator routeDefinitionLocator;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public DynamicRoutingController(RouteDefinitionWriter routeDefinitionWriter,
                                    RouteDefinitionLocator routeDefinitionLocator,
                                    ApplicationEventPublisher eventPublisher) {
        this.routeDefinitionWriter = routeDefinitionWriter;
        this.routeDefinitionLocator = routeDefinitionLocator;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/updateRoute")
    public Mono<Void> updateRoute(@RequestBody RouteDefinition routeDefinition) throws NacosException {
        // 删除旧的路由规则
        routeDefinitionWriter.delete(Mono.just(routeDefinition.getId()));

        // 添加新的路由规则
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();

        // 触发动态路由更新
        eventPublisher.publishEvent(new RefreshRoutesEvent(this));

        return Mono.empty();
    }

    @GetMapping("/routes")
    public Flux<RouteDefinition> getRoutes() {
        return routeDefinitionLocator.getRouteDefinitions();
    }
}