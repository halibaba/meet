package com.meet.netty.webSocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;

import java.time.LocalDate;

//TextWebSocketFrame表示一个文本帧
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息" + ctx.channel().remoteAddress() + ":" + msg.text());

        //回复客户端
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器事件" + LocalDate.now() + ":" + msg.text()));
    }


    //当web客户端连接后 触发方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id表示唯一的一个值，LongText是唯一的
        System.out.println("handlerAdded被调用" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded被调用" + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生" + cause.getMessage());
        ctx.close();
    }
}
