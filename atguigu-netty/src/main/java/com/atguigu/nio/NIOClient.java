package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void main(String[] args) throws IOException {
        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞模式
        socketChannel.configureBlocking(false);
        // 提供服务器端 ip, port
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        // 可以连接服务器了
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间, 客户端不会阻塞, 可以坐其他工作");
            }
        }
        // 如果连接成功, 就发送数据
        String str = "hello, 尚硅谷";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        // 发送数据, 将 buffer 数据写入 channel
        socketChannel.write(buffer);
        System.in.read();
    }

}
