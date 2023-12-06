package com.miaoqi.netgram.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO 聊天室接收器, 负责接收客户端连接, BIO 阻塞式模型就阻塞在接收器这里了, 所以需要启动多个线程处理任务
 *
 * @author miaoqi
 * @date 2019/11/26
 */
public class ChatServer {

    private final int DEFAULT_PORT = 8888;
    private final String QUIT = "quit";

    private ExecutorService executorService;
    private ServerSocket serverSocket;
    private Map<Integer, Writer> connectedClients;

    public ChatServer() {
        executorService = Executors.newFixedThreadPool(10);
        this.connectedClients = new HashMap<>();
    }

    public synchronized void addClient(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.connectedClients.put(port, writer);
            System.out.println("客户端[" + port + "]已经连接到服务器");
        }
    }

    public synchronized void removeClient(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            if (this.connectedClients.containsKey(port)) {
                this.connectedClients.get(port).close();
            }
            this.connectedClients.remove(port);
            System.out.println("客户端[" + port + "]已断开连接");
        }
    }

    public synchronized void forwardMessage(Socket socket, String msg) throws IOException {
        for (Integer port : connectedClients.keySet()) {
            if (!port.equals(socket.getPort())) {
                Writer writer = this.connectedClients.get(port);
                writer.write(msg);
                writer.flush();
            }
        }
    }

    public boolean readyToQuit(String msg) {
        return QUIT.equalsIgnoreCase(msg);
    }

    public synchronized void close() {
        if (this.serverSocket != null) {
            try {
                this.serverSocket.close();
                System.out.println("关闭 ServerSocket");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        try {
            this.serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("启动服务器, 监听端口: " + DEFAULT_PORT + "...");
            while (true) {
                // 等待客户端连接
                Socket socket = this.serverSocket.accept();
                // 创建 ChatHandler 线程
                // new Thread(new ChatHandler(this, socket)).start();
                executorService.execute(new ChatHandler(this, socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }

}
