package com.miaoqi.netgram.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        final int DEFAULT_PORT = 8888;
        ServerSocket serverSocket = null;
        final String QUIT = "quit";

        try {
            // 1. 创建 ServerSocket
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("启动服务器, 监听端口: " + DEFAULT_PORT);
            while (true) {
                // 2. 阻塞式等待客户端连接
                Socket socket = serverSocket.accept();
                // socket.getPort() 客户端的端口
                System.out.println("客户端[" + socket.getPort() + "]已连接");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String msg;
                while ((msg = reader.readLine()) != null) {
                    // 3. 阻塞式读取客户端消息, 只会读取换行符之前的数据
                    System.out.println("客户端[" + socket.getPort() + "]: " + msg);
                    // 4. 回复客户发送的消息
                    writer.write("服务器: " + msg + "\n");
                    writer.flush();

                    // 查看客户端是否退出
                    if (QUIT.equals(msg)) {
                        System.out.println("客户端[" + socket.getPort() + "]已断开连接");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 5. 关闭资源
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                    System.out.println("关闭 serverSocket");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
