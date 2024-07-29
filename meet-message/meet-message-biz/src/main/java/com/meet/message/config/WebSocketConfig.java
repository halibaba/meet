package com.meet.message.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @program: workdata-message
 * @ClassName WebSocketConfig
 * @description:
 * @author: MT
 * @create: 2022-12-15 13:46
 **/
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter ();
    }
}