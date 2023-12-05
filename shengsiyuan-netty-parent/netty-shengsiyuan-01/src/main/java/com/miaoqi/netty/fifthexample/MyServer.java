package com.miaoqi.netty.fifthexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

public class MyServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup parentEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup childEventLoopGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentEventLoopGroup, childEventLoopGroup).channel(
                    NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO)).childHandler(
                    new WebSocketChannelInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(8899)).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            parentEventLoopGroup.shutdownGracefully();
            childEventLoopGroup.shutdownGracefully();
        }
    }

}
