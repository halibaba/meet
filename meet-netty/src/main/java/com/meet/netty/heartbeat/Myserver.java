package com.meet.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class Myserver {
    public static void main(String[] args) {
        //创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))  //在bossGroup增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入一个netty提供的IdleStateHandler 这是一个处理空闲状态的处理器
                            /**
                             * readerldleTime 表示多长时间没有读操作了，就会发送一个心跳检测包检测是否还处于连接状态
                             * writerldlerTime 表示多长时间没有写操作了
                             * alldleTime 表示多长时间既没有读也没有写
                             */
                            //当IdleStateHandler触发后，就会传递给管道的下一个handler
                            //通过回调触发下一个handler的userEventTrigger，在该方法中处理这个IdleStateHandler事件
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            //加入一个对空闲检测进一步处理的handler（自定义）
                            pipeline.addLast(new MyserverHandler());
                        }
                    })
            ;

            //启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){

        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
