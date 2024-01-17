package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道就绪时就会触发该方法
     *
     * @author miaoqi
     * @date 2024-01-17 17:12:55
     *
     * @return
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client: " + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server: 喵", CharsetUtil.UTF_8));
    }

    /**
     * 当通道有读取事件时
     *
     * @author miaoqi
     * @date 2024-01-17 17:14:50
     *
     * @return
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务器回复的消息: " + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器端地址: " + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
