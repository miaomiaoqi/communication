package com.atguigu.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        // 创建 BossGroup 和 WorkerGroup
        // 1. 创建了两个线程组 bossGroup 和 workerGroup
        // 2. bossGroup 只是处理连接请求, 真正的和客户端的业务处理, 会交给 workerGroup
        // 3. 两个都是无限循环
        // 4. bossGroup 和 workerGroup 含有的子线程(NioEventLoop)的个数默认是 cpu 核数 * 2
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端启动对象, 配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 使用链式编程来进行设置
            bootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用 NioSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列等待连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道初始化对象(匿名对象)
                        // 向 pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new NettyServerHandler());
                        }
                    }); // 给我们的 WorkerGroup 的 EventLopp 的管道设置处理器

            System.out.println("...服务器 is ready");

            // 绑定一个端口, 并且同步, 生成了一个 ChannelFuture 对象
            // 启动服务器(并绑定端口)
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();

            // 给 channelFuture 注册监听器, 监控我们关心的事件
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("监听端口 6668 成功");
                    } else {
                        System.out.println("监听端口失败");
                    }
                }
            });

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
