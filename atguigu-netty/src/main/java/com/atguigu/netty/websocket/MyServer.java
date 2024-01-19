package com.atguigu.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class MyServer {

    public static void main(String[] args) throws Exception {
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
                            // 因为基于 http 协议, 使用 http 的编码解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 是以块的方式写, 添加 ChunkedWritedHandler 处理器
                            // 1. http 数据传输过程中是分段的, HttpObjectAggregator 就是可以将多个段聚合
                            // 2. 这就是为什么, 当浏览器发送大量数据时, 就会发出多次 http 请求
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            // 1. 对于 websocket 数据是以帧(frame) 形式传递
                            // 2. 可以看到 WebSocketFrame 下面有 6 个子类
                            // 3. 浏览器请求时 ws://localhost:7000/xxx 表示请求的 uri
                            // 4. WebSocketServerProtocolHandler 核心功能是将 http 协议升级为 ws 协议, 保持长连接
                            // 5. 是通过一个状态码 101
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            // 自定义的 handler, 处理业务逻辑
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
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
