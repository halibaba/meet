package com.meet.netty.groupChatTest2;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Map<String, Channel> userChannelMap = new ConcurrentHashMap<>();
    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channelGroup.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
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
            channelGroup.writeAndFlush("[系统] 用户 " + leavingUserId + " 离线了\n");
        }

        channelGroup.remove(leavingChannel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();

        if (msg.startsWith("/login ")) {
            String userId = msg.substring(7).trim();
            userChannelMap.put(userId, channel);
            channel.writeAndFlush("[系统] 登录成功，欢迎你：" + userId + "\n");
            return;
        }

        if (msg.startsWith("/to ")) {
            String[] parts = msg.split(" ", 3);
            if (parts.length < 3) {
                channel.writeAndFlush("[系统] 私聊格式错误，应为：/to userId 消息内容\n");
                return;
            }

            String toUserId = parts[1];
            String content = parts[2];
            Channel toChannel = userChannelMap.get(toUserId);

            if (toChannel != null) {
                toChannel.writeAndFlush("[私聊] 来自 " + getUserIdByChannel(channel) + "：" + content + "\n");
                channel.writeAndFlush("[私聊] 你对 " + toUserId + " 说：" + content + "\n");
            } else {
                channel.writeAndFlush("[系统] 用户 " + toUserId + " 不在线\n");
            }

            return;
        }

        String senderId = getUserIdByChannel(channel);
        for (Channel ch : channelGroup) {
            if (ch != channel) {
                ch.writeAndFlush("[群聊] " + senderId + ": " + msg + "\n");
            } else {
                ch.writeAndFlush("[自己] " + msg + "\n");
            }
        }
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
