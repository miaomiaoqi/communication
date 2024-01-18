package com.atguigu.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

public class GroupChatServerHandler extends SimpleChannelInboundHandler {

    // 定义一个 channel 组, 管理所有的 channel
    // GlobalEventExecutor.INSTANCE 是一个全局的事件执行器, 是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 连接建立, 第一个被执行
     * }将当前 channel 加入到 channelGroup
     *
     * @author miaoqi
     * @date 2024-01-18 20:0:11
     *
     * @return
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户加入聊天的信息推送给其他在线的客户端
        // 该方法会将 channelGroup 中所有的 channel 遍历并发送信息, 不需要自己遍历
        channelGroup.write("[客户端]" + channel.remoteAddress() + " 加入聊天\n");
        channelGroup.add(channel);
    }

    /**
     * 表示 channel 处于活动状态, 提示 xx 上线
     *
     * @author miaoqi
     * @date 2024-01-18 20:3:0
     *
     * @return
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了");
    }

    /**
     * 断开连接了, 将 xx 客户离开的信息推送给当前在线的客户
     *
     * @author miaoqi
     * @date 2024-01-18 20:4:19
     *
     * @return
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 离开了\n");
    }

    /**
     * 处于不活动状态, xx 离线了
     *
     * @author miaoqi
     * @date 2024-01-18 20:3:43
     *
     * @return
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线了");
    }

    /**
     * 读取数据
     *
     * @author miaoqi
     * @date 2024-01-18 20:6:17
     *
     * @return
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取到当前的 channel
        Channel channel = ctx.channel();
        // 这时我们遍历 channelGroup, 根据不同的情况回送不同的消息
        channelGroup.forEach(ch -> {
            if (channel != ch) { // 不是当前的 channel
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送了消息: " + msg + "\n");
            } else {
                ch.writeAndFlush("[自己]发送了消息: " + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
    }

}
