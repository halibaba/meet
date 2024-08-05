package com.meet.gateway.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @program: meet-boot
 * @ClassName MeetGatewayFilterFactory
 * @description: 需要在yml配置中配置：：
 *       routes:
 *         - id: admin_routh    #路由的ID，没有固定规则但要求唯一，建议配合服务名
 *           uri: lb://meet-admin-biz
 *           predicates:
 *             - Path=/**      #断言，路径相匹配的进行路由
 *           filters:
 *             - Meet=atguigu
 *             - Meet=X-Request-atguigu1,atguiguValue1
 * @author: MT
 * @create: 2024-07-31 23:01
 **/
@Component
public class MeetGatewayFilterFactory extends AbstractGatewayFilterFactory<MeetGatewayFilterFactory.Config> {

    public MeetGatewayFilterFactory(){
        super(MeetGatewayFilterFactory.Config.class);
    }

    @Override
    public GatewayFilter apply(MeetGatewayFilterFactory.Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                ServerHttpRequest request = exchange.getRequest();
                System.out.println("进入了自定义网关过滤器MyGatewayFilterFactory. status: " + config.getStatus());
                if (request.getQueryParams().containsKey("atguigu")) {
                    return chain.filter(exchange);
                } else {
                    exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                    return exchange.getResponse().setComplete();
                }
            }
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("status");
    }

    @Getter@Setter
    public static class Config {
        private String status;
    }
}