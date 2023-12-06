package com.miaoqi.netgram.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ChatClient {

    private static final String LOCALHOST = "localhost";
    private static final int DEFAULT_PORT = 8888;
    private static final String QUIT = "quit";
    private static final int BUFFER = 1024;

    private String host;
    private int port;
    private AsynchronousSocketChannel clientChannel;
    private Charset charset = Charset.forName("UTF-8");

    public ChatClient() {
        this(LOCALHOST, DEFAULT_PORT);
    }

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
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
            // 创建 channel
            this.clientChannel = AsynchronousSocketChannel.open();
            Future<Void> future = this.clientChannel.connect(new InetSocketAddress(this.host, this.port));
            // 阻塞式等待连接返回
            future.get();

            // 处理用户输入
            new Thread(new UserInputHandler(this)).start();

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER);
            while (true) {
                Future<Integer> readResult = this.clientChannel.read(buffer);
                int result = readResult.get();
                if (result <= 0) {
                    // 服务器异常
                    System.out.println("服务器断开");
                    close(clientChannel);
                    System.exit(1);
                } else {
                    buffer.flip();
                    String msg = String.valueOf(charset.decode(buffer));
                    buffer.clear();
                    System.out.println(msg);
                }
            }

        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
        if (msg.isEmpty()) {
            return;
        }
        ByteBuffer buffer = charset.encode(msg);
        Future<Integer> writeResult = clientChannel.write(buffer);
        try {
            writeResult.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("发送消息失败");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient("127.0.0.1", 7777);
        chatClient.start();
    }

}
