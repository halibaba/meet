package com.meet.netty.groupChatTest2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChatClient {

    private final String host;
    private final int port;

    public GroupChatClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void run(){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("decoder", new StringDecoder());
                    pipeline.addLast("encoder", new StringEncoder());
                    pipeline.addLast(new GroupChatClientHandler());
                }
            });

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            System.out.println("------" + channel.localAddress() + "-------");

            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入你的用户ID：");
            String userId = scanner.nextLine();
            channel.writeAndFlush("/login " + userId + "\r\n");

            while (scanner.hasNext()){
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg + "\r\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new GroupChatClient("127.0.0.1", 7000).run();
    }
}