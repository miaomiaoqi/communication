package com.miaoqi.netty.thirdexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyChatServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup parentLoopGroup = new NioEventLoopGroup();
        EventLoopGroup childLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentLoopGroup, childLoopGroup).channel(NioServerSocketChannel.class).childHandler(
                    new MyChatServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            parentLoopGroup.shutdownGracefully();
            childLoopGroup.shutdownGracefully();
        }
    }

}
