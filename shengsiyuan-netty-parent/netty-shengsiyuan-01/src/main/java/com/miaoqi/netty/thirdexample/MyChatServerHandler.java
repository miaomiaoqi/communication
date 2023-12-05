package com.miaoqi.netty.thirdexample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 保存所有与服务端建立连接的客户端对象
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 发送消息
     *
     * @author miaoqi
     * @date 2019-09-25
     *
     * @param ctx
     * @param msg
     *
     * @return
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush(channel.remoteAddress() + " 发送的消息: " + msg + "\n");
            }else{
                ch.writeAndFlush("[自己]" + msg + "\n");
            }
        });
    }

    /**
     * 服务端与客户端建立连接
     *
     * @author miaoqi
     * @date 2019-09-25
     *
     * @param ctx
     *
     * @return
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 广播通知其他客户端
        channelGroup.writeAndFlush("[服务器] - " + channel.remoteAddress() + "加入\n");
        // 保存客户端通道
        channelGroup.add(channel);
    }

    /**
     * 连接断开
     *
     * @author miaoqi
     * @date 2019-09-25
     *
     * @param ctx
     *
     * @return
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 移除 channel, 这行代码 netty 会自动调用
        // channelGroup.remove(channel);
        // 广播通知其他客户端
        channelGroup.writeAndFlush("[服务器] - " + channel.remoteAddress() + "离开\n");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "下线了");
    }

    /**
     * 发生异常
     *
     * @author miaoqi
     * @date 2019-09-25
     *
     * @param ctx
     * @param cause
     *
     * @return
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
