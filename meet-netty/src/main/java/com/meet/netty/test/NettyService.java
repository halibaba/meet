package com.meet.netty.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyService {

    public static void main(String[] args) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程进行设置
            bootstrap.group(bossGroup, workerGroup)  //设置两个线程组
                    .channel(NioServerSocketChannel.class)   //使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)  //设置线程队列
                    .childOption(ChannelOption.SO_KEEPALIVE, true)  //设置保持活动链接状态
                    .handler(null)//对应bossGroup
                    .childHandler(new ChannelInitializer<SocketChannel>() {   //创建一个通道测试对象（匿名对象） 对应workerGroup
                        //
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });  //给workerGroup的EventLoop对应的管道设置处理器

            System.out.println("服务器准备好了");

            //绑定一个端口并且同步，生成一个ChannelFuture对象
            //启动服务器并绑定端口
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
