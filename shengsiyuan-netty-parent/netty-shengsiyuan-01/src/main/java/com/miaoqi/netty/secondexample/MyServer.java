package com.miaoqi.netty.secondexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyServer {

    public static void main(String[] args) throws InterruptedException {
        // 接收线程
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        // 处理线程
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            // 服务端启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup).channel(NioServerSocketChannel.class).childHandler(
                    new MyServerInitializer());
            // 绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

}