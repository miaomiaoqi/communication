package com.miaoqi.netgram.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private final String LOCALHOST = "localhost";
    private final int DEFAULT_PORT = 8888;
    AsynchronousServerSocketChannel serverChannel;

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                System.out.println("关闭" + closeable);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        try {
            // 绑定监听端口
            // AsynchronousChannelGroup, 所有需要用到线程的地方, 都会从组中获取
            this.serverChannel = AsynchronousServerSocketChannel.open();
            this.serverChannel.bind(new InetSocketAddress(this.LOCALHOST, this.DEFAULT_PORT));
            System.out.println("启动服务器, 监听端口: " + this.DEFAULT_PORT);

            while (true) {
                this.serverChannel.accept(null, new AcceptHandler());
                // 因为 accept 是异步调用的, 使用阻塞式方法防止主线程结束
                System.in.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close(this.serverChannel);
        }
    }

    /**
     * AIO 的一种回调函数, 使用 CompletionHandler 接口
     *
     * @author miaoqi
     * @date 2019/12/10
     */
    private class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

        // 异步调用正常返回
        // result: 客户端 channel
        // attachment: 服务端自己传入的附件, 相当于异步的传参
        @Override
        public void completed(AsynchronousSocketChannel result, Object attachment) {
            // 继续监听
            if (Server.this.serverChannel.isOpen()) {
                Server.this.serverChannel.accept(null, this);
            }
            AsynchronousSocketChannel clientChannel = result;
            if (clientChannel != null && clientChannel.isOpen()) {
                // 处理客户端通道上的读写操作, 异步回调函数
                ClientHandler handler = new ClientHandler(clientChannel);

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // 处理读操作时的附加信息, 此处是演示用
                Map<String, Object> info = new HashMap<>();
                info.put("type", "read");
                info.put("buffer", buffer);
                // 从客户端通道异步读取数据
                clientChannel.read(buffer, info, handler);
            }
        }

        // 异步调用出现错误
        @Override
        public void failed(Throwable exc, Object attachment) {
            // 处理错误
        }

    }

    /**
     * 客户端回调函数, 既能处理写回调, 又能处理读回调
     *
     * @author miaoqi
     * @date 2019/12/9
     */
    // 第一个泛型是异步函数返回结果的类型
    // 第二个泛型是 attachment 的类型, 即客户端想通过回调函数带回的参数
        // A 调用 B 传入 attachment, B 在回调函数时, 将 attachment 传入
    private class ClientHandler implements CompletionHandler<Integer, Object> {

        private AsynchronousSocketChannel clientChannel;

        public ClientHandler(AsynchronousSocketChannel channel) {
            this.clientChannel = channel;
        }

        @Override
        public void completed(Integer result, Object attachment) {
            Map<String, Object> info = (Map<String, Object>) attachment;
            String type = (String) info.get("type");
            if ("read".equalsIgnoreCase(type)) {
                // 通过附加信息判断是读操作的回调, 那么就将消息原路写回给客户端
                ByteBuffer buffer = (ByteBuffer) info.get("buffer");
                buffer.flip();
                info.put("type", "write");
                clientChannel.write(buffer, info, this);
                buffer.clear();
            } else if ("write".equalsIgnoreCase(type)) {
                // 服务端将客户端的消息原封不动发还给客户端后的回调, 继续监听读操作
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // 处理读操作时的附加信息, 此处是演示用
                info.put("type", "read");
                info.put("buffer", buffer);
                // 从客户端通道异步读取数据
                clientChannel.read(buffer, info, this);
            }
        }

        @Override
        public void failed(Throwable exc, Object attachment) {
            // 错误处理
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

}
