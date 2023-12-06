package com.miaoqi.netgram.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

public class ChatClient {

    private static final String DEFAULT_SERVER_HOST = "127.0.0.1";
    private static final int DEFAULT_SERVER_PORT = 8888;
    private static final String QUIT = "quit";

    private static final int BUFFER = 1024;
    private String host;
    private int port;
    private SocketChannel client;
    private ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER);
    private ByteBuffer wBuffer = ByteBuffer.allocate(BUFFER);
    private Selector selector;
    private Charset charset = Charset.forName("UTF-8");

    public ChatClient() {
        this(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);
    }

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean readyToQuit(String msg) {
        return QUIT.equalsIgnoreCase(msg);
    }

    public static void close(Closeable closeable) {
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
            this.client = SocketChannel.open();
            this.client.configureBlocking(false);

            this.selector = Selector.open();

            this.client.register(this.selector, SelectionKey.OP_CONNECT);
            this.client.connect(new InetSocketAddress(this.host, this.port));

            while (true) {
                this.selector.select();
                Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                for (SelectionKey key : selectionKeys) {
                    this.handles(key);
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClosedSelectorException e) {
            // 用户正常退出
        } finally {
            // selector 可以反复被关闭, 不会报错
            close(selector);
        }
    }

    private void handles(SelectionKey key) throws IOException {
        // CONNECT 事件 - 连接就绪事件
        if (key.isConnectable()) {
            SocketChannel client = (SocketChannel) key.channel();
            if (client.isConnectionPending()) {
                // 真正的建立了连接
                client.finishConnect();
                // 处理用户的输入
                new Thread(new UserInputHandler(this)).start();
            }
            client.register(this.selector, SelectionKey.OP_READ);
        }
        // READ 事件 - 服务器转发过来的消息
        else if (key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();
            String msg = this.receive(client);
            if (msg.isEmpty()) {
                // 服务器异常, 因为理论上发过来的数据就不可能是空数据
                close(this.selector);
            } else {
                System.out.println(msg);
            }
        }
    }

    public void send(String msg) throws IOException {
        if (msg.isEmpty()) {
            return;
        }
        this.wBuffer.clear();
        this.wBuffer.put(this.charset.encode(msg));
        this.wBuffer.flip();
        while (this.wBuffer.hasRemaining()) {
            this.client.write(this.wBuffer);
        }
        // 检查用户是否准备退出
        if (this.readyToQuit(msg)) {
            close(this.selector);
        }
    }

    private String receive(SocketChannel client) throws IOException {
        this.rBuffer.clear();
        // socket 没有结束符, 所以只要不是 0 就代表还有数据
        while (client.read(this.rBuffer) > 0) {
        }
        this.rBuffer.flip();
        return String.valueOf(this.charset.decode(this.rBuffer));
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient("127.0.0.1", 7777);
        client.start();
    }

}
