package com.atguigu.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {

    public static void main(String[] args) throws InterruptedException {
        // 创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 8 个 EventLoop
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup) //
                    .channel(NioServerSocketChannel.class) //
                    .handler(new LoggingHandler(LogLevel.INFO)) // 在 bossGroup 增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 加入一个 netty 提供的 IdleStateHandler
                            // IdleStateHandler 是 netty 提供的处理空闲状态的处理器
                            // long readerIdleTime 表示多长时间没有读, 就会发送一个心跳检测包检测是否连接状态
                            // long writerIdleTime 表示多长时间没有写, 就会发送一个心跳检测包检测是否连接状态
                            // long allIdleTime 表示多长时间没有读写, 就会发送一个心跳检测包检测是否连接状态
                            // 当 IdleStateHandler 触发后就会传递给管道的下一个 handler 去处理, 通过调用下一个 handler 的 userEventTriggerd, 在该方法种处理
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的 handler(自定义)
                            pipeline.addLast(new MyServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
