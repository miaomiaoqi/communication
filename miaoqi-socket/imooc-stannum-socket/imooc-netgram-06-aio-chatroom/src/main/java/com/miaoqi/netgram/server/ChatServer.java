package com.miaoqi.netgram.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private static final String LOCALHOST = "localhost";
    private static final int DEFAULT_PORT = 8888;
    private static final String QUIT = "quit";
    private static final int BUFFER = 1024;
    private static final int THREADPOOL_SIZE = 8;

    private AsynchronousChannelGroup channelGroup;
    private AsynchronousServerSocketChannel serverChannel;
    private List<ClientHandler> connectedClients;
    private Charset charset = Charset.forName("UTF-8");
    private int port;

    public ChatServer() {
        this(DEFAULT_PORT);
    }

    public ChatServer(int port) {
        this.port = port;
        this.connectedClients = new ArrayList<>();
    }

    public boolean readyToQuit(String msg) {
        return QUIT.equalsIgnoreCase(msg);
    }

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void start() {
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(THREADPOOL_SIZE);
            this.channelGroup = AsynchronousChannelGroup.withThreadPool(executorService);
            // 手动指定 channelGroup, 如果不指定会使用系统默认的, 即共享线程池
            this.serverChannel = AsynchronousServerSocketChannel.open(this.channelGroup);
            this.serverChannel.bind(new InetSocketAddress(LOCALHOST, this.port));
            System.out.println("启动服务器, 监听端口: " + this.port);

            while (true) {
                this.serverChannel.accept(null, new AcceptHandler());
                System.in.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close(this.serverChannel);
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer(7777);
        server.start();
    }

    private class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

        @Override
        public void completed(AsynchronousSocketChannel clientChannel, Object attachment) {
            if (ChatServer.this.serverChannel.isOpen()) {
                // 继续监听连接请求
                ChatServer.this.serverChannel.accept(null, this);
            }
            if (clientChannel != null && clientChannel.isOpen()) {
                ClientHandler handler = new ClientHandler(clientChannel);
                ByteBuffer buffer = ByteBuffer.allocate(ChatServer.BUFFER);
                // 将新用户添加到在线用户列表中
                ChatServer.this.addClient(handler);
                clientChannel.read(buffer, buffer, handler);
            }
        }

        @Override
        public void failed(Throwable exc, Object attachment) {
            System.out.println("连接失败: " + exc);
        }

    }

    private synchronized void addClient(ClientHandler handler) {
        this.connectedClients.add(handler);
        System.out.println(this.getClientName(handler.clientChannel) + "已连接到服务器");
    }

    private synchronized void removeClient(ClientHandler handler) {
        this.connectedClients.remove(handler);
        System.out.println(this.getClientName(handler.clientChannel) + "已断开连接");
        // 通知客户端通道已经关闭了
        this.close(handler.clientChannel);
    }

    private class ClientHandler implements CompletionHandler<Integer, Object> {

        public AsynchronousSocketChannel clientChannel;

        public ClientHandler(AsynchronousSocketChannel clientChannel) {
            this.clientChannel = clientChannel;
        }

        @Override
        public void completed(Integer result, Object attachment) {
            ByteBuffer buffer = (ByteBuffer) attachment;
            if (buffer != null) {
                // 只有在读操作时, 才传入 attachment 对象
                if (result <= 0) {
                    // 客户端通道发生异常
                    // 将客户端从在线客户列表中移除
                    ChatServer.this.removeClient(this);
                } else {
                    buffer.flip();
                    String fwdMsg = ChatServer.this.receive(buffer);
                    System.out.println(ChatServer.this.getClientName(this.clientChannel) + ": " + fwdMsg);
                    ChatServer.this.forwardMessage(this.clientChannel, fwdMsg);
                    buffer.clear();
                    // 检测用户是否退出
                    if (ChatServer.this.readyToQuit(fwdMsg)) {
                        ChatServer.this.removeClient(this);
                    } else {
                        // 再次监听 read 方法
                        this.clientChannel.read(buffer, buffer, this);
                    }
                }
            }
        }

        @Override
        public void failed(Throwable exc, Object attachment) {
            System.out.println("读写失败: " + exc);
        }

    }

    private synchronized void forwardMessage(AsynchronousSocketChannel clientChannel, String fwdMsg) {
        for (ClientHandler handler : this.connectedClients) {
            if (!clientChannel.equals(handler.clientChannel)) {
                try {
                    ByteBuffer buffer = this.charset.encode(this.getClientName(clientChannel) + ": " + fwdMsg);
                    handler.clientChannel.write(buffer, null, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String getClientName(AsynchronousSocketChannel clientChannel) {
        int clientPort = -1;
        try {
            InetSocketAddress address = (InetSocketAddress) clientChannel.getRemoteAddress();
            clientPort = address.getPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "客户端[" + clientPort + "]";
    }

    private String receive(ByteBuffer buffer) {
        CharBuffer charBuffer = this.charset.decode(buffer);
        return String.valueOf(charBuffer);
    }

}
