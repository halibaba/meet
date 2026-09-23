package com.meet.netty.groupChatTest;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //私聊思路
    private static Map<String, Channel> userChannelMap = new HashMap<String, Channel>();

    //定义一个channel组 管理所有的channel
    //GlobalEventExecutor.INSTANCE是全局的事件执行器 是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //表示连接建立 一旦连接 第一个被执行
    //将当前channel加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端
        //该方法会将channelGroup中所有的channel遍历，并发送，因此不需要自己遍历
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入聊天\n");
        System.out.println("[客户端]" + channel.remoteAddress() + "加入聊天\n");
        channelGroup.add(channel);


    }

    //表示断开连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离开了\n");
        System.out.println("[客户端]" + channel.remoteAddress() + "离开了\n");
        System.out.println("channelGroup size" + channelGroup.size());
    }

    //表示channel处于活动状态，提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "上线了\n");
        System.out.println("[客户端]" + channel.remoteAddress() + "上线了\n");
    }

    //表示channel处于不活动状态，提示xx下线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离线了\n");
        System.out.println("[客户端]" + channel.remoteAddress() + "离线了\n");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取到当前channel
        Channel channel = ctx.channel();
        //过滤自己
        channelGroup.forEach(ch -> {
            if(channel != ch){ //不是当前channel，转发消息
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + "发送了消息：" + msg + "\n");
            }else {
                ch.writeAndFlush("[自己]发送了消息" + msg);
            }
        });
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
