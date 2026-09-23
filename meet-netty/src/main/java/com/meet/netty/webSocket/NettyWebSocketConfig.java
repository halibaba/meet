package com.meet.netty.webSocket;

import com.meet.netty.webSocket.WebSocketServer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NettyWebSocketConfig implements DisposableBean {

    private WebSocketServer server;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        server = new WebSocketServer();
        new Thread(server::start).start();
    }

    @Override
    public void destroy() {
        if (server != null) {
            server.stop(); // 调用你自己写的停止方法
        }
    }
}