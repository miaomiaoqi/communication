package com.miaoqi.netgram.client;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Client {

    private final String LOCALHOST = "localhost";
    private final int DEFAULT_PORT = 8888;
    AsynchronousSocketChannel clientChannel;

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                System.out.println("关闭: " + closeable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        try {
            // 创建 Channel
            this.clientChannel = AsynchronousSocketChannel.open();
            // AIO 的一种回调函数, 使用 Future 接口
            Future<Void> future = this.clientChannel.connect(new InetSocketAddress(this.LOCALHOST, this.DEFAULT_PORT));
            // 阻塞式调用, 等待连接真正建立
            future.get();

            // 等到用户输入
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String input = consoleReader.readLine();
                byte[] inputByte = input.getBytes();
                ByteBuffer buffer = ByteBuffer.wrap(inputByte);
                Future<Integer> writeResult = this.clientChannel.write(buffer);
                // 发送给服务器
                writeResult.get();
                buffer.clear();
                Future<Integer> readResult = this.clientChannel.read(buffer);
                readResult.get();
                String echo = new String(buffer.array());
                buffer.clear();
                System.out.println(echo);
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            this.close(this.clientChannel);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

}
