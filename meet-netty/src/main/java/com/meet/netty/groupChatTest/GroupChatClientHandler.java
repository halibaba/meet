package com.meet.netty.groupChatTest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GroupChatClientHandler extends SimpleChannelInboundHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg.toString().trim());
    }
}
