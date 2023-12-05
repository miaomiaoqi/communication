package com.miaoqi.netty.fourthexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 心跳demo, 客户端使用 thirdexample 包下的 client
 *
 * @author miaoqi
 * @date 2019/9/25
 */
public class MyServer {

    public static void main(String[] args) throws Exception {
        EventLoopGroup parentEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup childEventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentEventLoopGroup, childEventLoopGroup).channel(NioServerSocketChannel.class)
                    // handler 针对 parent 起作用
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // childHandler 针对 child 起作用
                    .childHandler(new MyServerInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            parentEventLoopGroup.shutdownGracefully();
            childEventLoopGroup.shutdownGracefully();
        }
    }

}