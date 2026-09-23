package com.meet.netty.webSocket2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
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
                            //因为基于http协议 使用http的编码的解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以块方式写 添加一个chunkedWrite处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /*
                            http数据在传输过程中是分段的 HttpObjectAggregator可以将多个段聚合起来
                            这就是为什么浏览器发送大量数据时就会发出多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /*
                            对于websocket 它的数据是以帧（frame）的形式传递
                            可以看到websocketframe下面有六个子类
                            浏览器请求时 ws://localhost:7000/hello 表示请求的uri
                            WebSocketServerProtocolHandler 核心功能是将http协议升级为ws协议，保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //自定义handler 处理业务逻辑
                            pipeline.addLast(new WebSocketServerHandler());

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
