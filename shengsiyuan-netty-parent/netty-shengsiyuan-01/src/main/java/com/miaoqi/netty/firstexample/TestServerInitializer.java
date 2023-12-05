package com.miaoqi.netty.firstexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 编解码
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        // 自定义处理器
        pipeline.addLast("testHttpServerHandler", new TestHttpServerHandler());
    }

}
