package com.atguigu.netty.simple;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 我们自定义一个 Handler 需要继承 netty 规定好的某个 HandlerAdapter
 * 这时我们自定义的一个 Handler, 才能被称为一个 Handler
 *
 * @author miaoqi
 * @date 2024-01-17 11:57:29
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据实际(这里我们可以读取客户端发送的消息)
     * ChannelHandlerContext ctx: 上下文对象, 含有管道 pipeline, 通道 channel, 地址
     * Object msg: 就是客户端发送的数据 默认 Object
     *
     * @author miaoqi
     * @date 2024-01-17 11:59:17
     *
     * @return
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 比如我们这里有一个非常耗时的业务 -> 异步执行 -> 提交该 channel 对应的 NIOEventLoop 的 taskQueue 中

        // 解决方案1: 用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(10000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端~22222", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                System.out.println("发生异常: " + e.getMessage());
            }
        });

        // 用户自定义定时任务 -> 该任务是提交到 scheduleTaskQueue
        ctx.channel().eventLoop().schedule(() -> {
            try {
                Thread.sleep(10000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端~444444", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                System.out.println("发生异常: " + e.getMessage());
            }
        }, 5, TimeUnit.SECONDS);

        System.out.println("go on...");

        // System.out.println("server ctx = " + ctx);
        // // 讲 msg 转成一个 ByteBuf
        // // ByteBuf 是 Netty 提供的, 不是 NIO 的 ByteBuffer
        // ByteBuf buf = (ByteBuf) msg;
        // System.out.println("客户端发送消息是: " + buf.toString(CharsetUtil.UTF_8));
        // System.out.println("客户端地址: " + ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕
     *
     * @author miaoqi
     * @date 2024-01-17 16:13:3
     *
     * @return
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // writeAndFlush 是 write + flush
        // 讲数据写入到缓冲并刷新到 channel 中
        // 一般来讲我们对这个发送的数据编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端~", CharsetUtil.UTF_8));
    }

    /**
     * 处理异常, 一般是需要关闭通道
     *
     * @author miaoqi
     * @date 2024-01-17 16:15:27
     *
     * @return
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
