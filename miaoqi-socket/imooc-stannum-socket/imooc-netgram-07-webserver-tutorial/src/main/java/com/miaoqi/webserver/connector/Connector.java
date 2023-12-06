package com.miaoqi.webserver.connector;

import com.miaoqi.webserver.processor.ServletProcessor;
import com.miaoqi.webserver.processor.StaticProcessor;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class Connector implements Runnable {


    private static final int DEFAULT_PORT = 8888;

    private ServerSocketChannel server;
    private Selector selector;
    private int port;

    public Connector() {
        this(DEFAULT_PORT);
    }

    public Connector(int port) {
        this.port = port;
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }


    @Override
    public void run() {
        try {
            // 开启一个 ServerSocketChannel 默认是阻塞式的
            this.server = ServerSocketChannel.open();
            // 关闭阻塞式
            this.server.configureBlocking(false);
            // 获取通道中的 ServerSocket 并绑定端口
            this.server.socket().bind(new InetSocketAddress(this.port));

            // 开启一个 Selector
            this.selector = Selector.open();
            // 将 channel 注册到 selector 上
            this.server.register(this.selector, SelectionKey.OP_ACCEPT);
            System.out.println("启动服务器, 监听端口: " + this.port + "...");

            while (true) {
                // 如果注册在这个 selector 上的所有通道都没有事件发生, 那么这个 select 方法会一直阻塞的, 直到有至少一个事件发生
                this.selector.select();
                // 这一次 select 函数调用所触发的所有事件
                Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                for (SelectionKey key : selectionKeys) {
                    // 处理所有被触发的事件
                    this.handles(key);
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭 selector 的时候会将关联的 channel 全部关闭
            this.close(this.selector);
        }
    }

    private void handles(SelectionKey key) throws IOException {
        // ACCEPT
        if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(this.selector, SelectionKey.OP_READ);
        } else {
            // READ
            SocketChannel client = (SocketChannel) key.channel();
            // 解除 socketChannel 与 selector 的关联, 获取阻塞状态的 I/O
            key.cancel();
            // 改为阻塞模式, 如果是非阻塞模式获取 I/O 会报错
            client.configureBlocking(true);
            Socket socket = client.socket();
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            Request request = new Request(input);
            request.parse();

            Response response = new Response(output);
            response.setRequest(request);

            if (request.getRequestURI().startsWith("/servlet/")) {
                ServletProcessor processor = new ServletProcessor();
                processor.process(request, response);
            } else {
                StaticProcessor processor = new StaticProcessor();
                processor.process(request, response);
            }
            close(client);
        }
    }

    private void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
