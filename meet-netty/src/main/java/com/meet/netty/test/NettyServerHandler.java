package com.meet.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * 1.自定义一个handler 需要继续netty 规定好的某个handlerAdapter
 * 2.这时我们自定义一个handler，才能称为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    //读取数据实际（这里是我们可以读取客户端发送的消息）
    /*
    1.ChannelHandlerContext ctx：上下文对象，含有管道pipeline，通道channel，地址
    2.Object msg：就是客户端发送的数据 默认object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("server ctx = " + ctx);
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址是" + ctx.channel().remoteAddress());

        //异步执行
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端2222!", CharsetUtil.UTF_8));
            }
        });



        System.out.println("go on...");
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓冲并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端1111!", CharsetUtil.UTF_8));
    }

    //处理异常,关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
