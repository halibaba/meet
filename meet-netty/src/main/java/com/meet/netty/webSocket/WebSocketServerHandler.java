package com.meet.netty.webSocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//TextWebSocketFrame表示一个文本帧
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用户ID与Channel的映射关系
    private static final Map<String, Channel> userChannelMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        Channel currentChannel = ctx.channel();

        System.out.println("服务器端收到消息: " + text);

        // 判断是否为登录请求（格式：/login userId）
        if (text.startsWith("/login ")) {
            String userId = text.substring(7).trim();
            userChannelMap.put(userId, currentChannel);
            currentChannel.writeAndFlush(new TextWebSocketFrame("[系统] 登录成功，您的ID是：" + userId));
            return;
        }

        // 判断是否为私聊请求（格式：/to userId 消息内容）
        if (text.startsWith("/to ")) {
            String[] parts = text.split(" ", 3);
            if (parts.length < 3) {
                currentChannel.writeAndFlush(new TextWebSocketFrame("[系统] 私聊格式错误，应为：/to userId 消息内容"));
                return;
            }

            String toUserId = parts[1];
            String content = parts[2];
            Channel toChannel = userChannelMap.get(toUserId);
            String fromUserId = getUserIdByChannel(currentChannel);

            if (toChannel != null) {
                toChannel.writeAndFlush(new TextWebSocketFrame("[私聊] 来自 " + fromUserId + "：" + content));
                currentChannel.writeAndFlush(new TextWebSocketFrame("[私聊] 你对 " + toUserId + " 说：" + content));
            } else {
                currentChannel.writeAndFlush(new TextWebSocketFrame("[系统] 用户 " + toUserId + " 不在线"));
            }
            return;
        }

        // 广播群发（不是 /login 也不是 /to）
        String senderId = getUserIdByChannel(currentChannel);
        for (Channel ch : userChannelMap.values()) {
            if (ch != currentChannel) {
                ch.writeAndFlush(new TextWebSocketFrame("[群聊] " + senderId + ": " + text));
            }
        }
        currentChannel.writeAndFlush(new TextWebSocketFrame("[自己] " + text));
    }

    private String getUserIdByChannel(Channel channel) {
        for (Map.Entry<String, Channel> entry : userChannelMap.entrySet()) {
            if (entry.getValue().equals(channel)) {
                return entry.getKey();
            }
        }
        return "匿名用户";
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("handlerAdded被调用 " + ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel leavingChannel = ctx.channel();
        String leavingUserId = null;

        for (Map.Entry<String, Channel> entry : userChannelMap.entrySet()) {
            if (entry.getValue().equals(leavingChannel)) {
                leavingUserId = entry.getKey();
                break;
            }
        }

        if (leavingUserId != null) {
            userChannelMap.remove(leavingUserId);
            System.out.println("用户 " + leavingUserId + " 离线了");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("异常发生: " + cause.getMessage());
        ctx.close();
    }
}