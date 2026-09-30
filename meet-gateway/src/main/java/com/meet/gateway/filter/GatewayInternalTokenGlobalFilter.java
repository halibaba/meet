package com.meet.gateway.filter;

import meet.pub.gateway.GatewayInternalTokenProperties;
import meet.pub.gateway.GatewayInternalTokens;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 去掉客户端自带的内部凭证，再写入网关自己的凭证。
 */
@Component
@ConditionalOnProperty(prefix = "meet.gateway", name = "internal-token")
public class GatewayInternalTokenGlobalFilter implements GlobalFilter, Ordered {

    private final GatewayInternalTokenProperties properties;

    public GatewayInternalTokenGlobalFilter(GatewayInternalTokenProperties properties) {
        this.properties = properties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(headers -> {
                    headers.remove(GatewayInternalTokens.HEADER);
                    headers.set(GatewayInternalTokens.HEADER, properties.getInternalToken());
                })
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
