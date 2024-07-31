package com.meet.gateway.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * @author wsk
 * @date 2020/3/15 18:10
 */
//@Component
@Slf4j
public class MeetGatewayFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 清洗请求头中from 参数
//        ServerHttpRequest request = exchange.getRequest().mutate().headers(httpHeaders -> {
//            httpHeaders.remove("from");
//            // 设置请求时间
//            httpHeaders.put("REQUEST-START-TIME",
//                    Collections.singletonList(String.valueOf(System.currentTimeMillis())));
//        }).build();

        // 2. 重写StripPrefix
//        addOriginalRequestUrl(exchange, request.getURI());
//        String rawPath = request.getURI().getRawPath();
//        String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/")).skip(1L)
//                .collect(Collectors.joining("/"));
//        ServerHttpRequest newRequest = request.mutate().path(newPath).build();
//        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());

//        return chain.filter(exchange.mutate().request(newRequest.mutate().build()).build());
//        return chain.filter(exchange);
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String url = request.getURI().getPath();
        if(!url.equals("/oauth/token")){
            String rawPath = request.getURI().getRawPath();
            String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/")).skip(1L)
                    .collect(Collectors.joining("/"));
            ServerHttpRequest newRequest = request.mutate().path(newPath).build();
            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());
            log.info(newPath+"不是登陆请求，向下放行！");
            return chain.filter(exchange.mutate().request(newRequest.mutate().build()).build());
//            return Mono.empty();
        }
        log.info("接收到登陆请求：{}", url);
        // 跨域放行Authorization
        if (request.getMethod() == HttpMethod.OPTIONS) {
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }
        // 授权
        if (!this.auth(exchange, chain)) {
            return this.responseBody(exchange, 406, "请先登录");
        }
        return chain.filter(exchange);
    }

    /**
     * 认证
     */
    private boolean auth(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 逻辑自行实现
        String token = this.getToken(exchange.getRequest());
        log.info("token:{}", token);
        return true;
    }

    /**
     * 获取token
     */
    public String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst("token");
        if (StringUtils.isEmpty(token)) {
            return request.getQueryParams().getFirst("token");
        }
        return token;
    }

    /**
     * 设置响应体
     **/
    public Mono<Void> responseBody(ServerWebExchange exchange, Integer code, String msg) {
        String message = JSON.toJSONString(null);
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        return this.responseHeader(exchange).getResponse()
                .writeWith(Flux.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }

    /**
     * 设置响应体的请求头
     */
    public ServerWebExchange responseHeader(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        return exchange.mutate().response(response).build();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}