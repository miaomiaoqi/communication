package com.miaoqi.netty.secondexample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * 泛型代表客户端真正发送来的数据类型
 *
 * @author miaoqi
 * @date 2019/9/11
 */
public class MyServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + ", " + msg);
        // ctx.channel().write("");
        // ctx.channel().flush();
        ctx.channel().writeAndFlush("from server: " + UUID.randomUUID().toString());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
