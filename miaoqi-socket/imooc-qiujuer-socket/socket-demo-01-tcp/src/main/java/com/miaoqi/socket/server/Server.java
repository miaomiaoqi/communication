package com.miaoqi.socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(2000);

        System.out.println("服务器准备就绪");
        System.out.println("服务端信息: " + serverSocket.getInetAddress() + " P: " + serverSocket.getLocalPort());

        // 等待客户端连接
        for(;;) {
            // 得到客户端
            Socket client = serverSocket.accept();
            // 客户端构建异步线程
            ClientHandler clientHandler = new ClientHandler(client);
            // 启动线程
            clientHandler.start();
        }
    }

    /**
     * 客户端消息处理
     *
     * @author miaoqi
     * @date 2019-03-14
     */
    private static class ClientHandler extends Thread{
        private Socket socket;
        private boolean flag = true;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("新客户端连接: " + socket.getInetAddress() + " P: " + socket.getPort());
            try {
                // 得到打印流, 用于数据输出, 服务器回送数据使用
                PrintStream socketOutput = new PrintStream(socket.getOutputStream());
                // 得到输入流, 接收客户端数据
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                do {
                    // 客户端得到一条数据
                    String line = socketInput.readLine();
                    if ("bye".equals(line)) {
                        flag = false;
                        // 回送bye
                        socketOutput.println("bye");
                    } else {
                        // 打印到屏幕, 并回送数据长度
                        System.out.println(line);
                        socketOutput.println("回送: " + line.length());
                    }
                } while (flag);
                socketInput.close();
                socketOutput.close();
            } catch (Exception e) {
                System.out.println("连接异常断开");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("客户端已退出: " + socket.getInetAddress() + " P: " + socket.getPort());
            }
        }
    }

}
