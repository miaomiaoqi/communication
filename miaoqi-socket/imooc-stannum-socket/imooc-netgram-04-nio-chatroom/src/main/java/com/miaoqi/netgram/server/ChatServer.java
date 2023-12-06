package com.miaoqi.netgram.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * BIO 聊天室接收器, 负责接收客户端连接, BIO 阻塞式模型就阻塞在接收器这里了, 所以需要启动多个线程处理任务
 *
 * @author miaoqi
 * @date 2019/11/26
 */
public class ChatServer {

    private static final int DEFAULT_PORT = 8888;
    private static final String QUIT = "quit";
    private static final int BUFFER = 1024;

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER);
    private ByteBuffer wBuffer = ByteBuffer.allocate(BUFFER);
    private Charset charset = Charset.forName("UTF-8");
    private int port;

    public ChatServer() {
        this(DEFAULT_PORT);
    }

    // 自定义端口
    public ChatServer(int port) {
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

    public void start() {
        try {
            // 开启一个 ServerSocketChannel 默认是阻塞式的
            this.serverSocketChannel = ServerSocketChannel.open();
            // 关闭阻塞式
            this.serverSocketChannel.configureBlocking(false);
            // 获取通道中的 ServerSocket 并绑定端口
            this.serverSocketChannel.socket().bind(new InetSocketAddress(this.port));

            // 开启一个 Selector
            this.selector = Selector.open();
            // 将 channel 注册到 selector 上
            this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
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
            close(this.selector);
        }
    }

    private void handles(SelectionKey key) throws IOException {
        // ACCEPT 事件 - 和客户端建立了连接, 在 ServerSocketChannel 上触发的事件
        if (key.isAcceptable()) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel client = serverSocketChannel.accept();
            client.configureBlocking(false);
            client.register(this.selector, SelectionKey.OP_READ);
            System.out.println(this.getClientName(client) + "已连接");
        }
        // READ 事件 - 客户端发送了消息给服务器端
        else if (key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();
            // 从通道中读取数据
            String fwdMsg = this.receive(client);
            if (fwdMsg.isEmpty()) {
                // 客户端异常, 客户端如果有消息过来, 就不应该是一个空消息
                // 取消这个 channel 的事件
                key.cancel();
                // 通知 Selector 强制执行一次 select 方法刷新状态
                this.selector.wakeup();
            } else {
                this.forwardMessage(client, fwdMsg);
                // 检查用户是否退出
                if (this.readyToQuit(fwdMsg)) {
                    key.cancel();
                    this.selector.wakeup();
                    System.out.println(this.getClientName(client) + "已断开");
                }
            }
        }
    }

    private void forwardMessage(SocketChannel client, String fwdMsg) throws IOException {
        // 返回所有注册在 Selector 上的 channel, 无论是什么状态
        Set<SelectionKey> keys = this.selector.keys();
        for (SelectionKey key : keys) {
            SelectableChannel connectedChannel = key.channel();
            if (connectedChannel instanceof ServerSocketChannel) {
                continue;
            }
            // 对应的 channel是一个连接状态, 没有被关闭, selector 本身也是一个正常的状态
            if (key.isValid() && !client.equals(connectedChannel)) {
                this.wBuffer.clear();
                this.wBuffer.put(this.charset.encode(this.getClientName(client) + ":" + fwdMsg));
                this.wBuffer.flip();
                while (this.wBuffer.hasRemaining()) {
                    ((SocketChannel) connectedChannel).write(this.wBuffer);
                }
            }
        }
    }

    private String receive(SocketChannel client) throws IOException {
        this.rBuffer.clear();
        // 与 FileChannel 不同, FileChannel 是操作文件, 有结束标识(-1)
        // SocketChannel 只要读不到数据了, 就是结束
        while (client.read(this.rBuffer) > 0) {
        }
        // 写模式转为读模式
        this.rBuffer.flip();
        return String.valueOf(this.charset.decode(this.rBuffer));
    }

    private String getClientName(SocketChannel client) {
        return "客户端[" + client.socket().getPort() + "]";
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(7777);
        chatServer.start();
    }

}
