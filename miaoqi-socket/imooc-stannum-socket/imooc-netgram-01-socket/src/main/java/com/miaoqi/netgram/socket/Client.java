package com.miaoqi.netgram.socket;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        final String QUIT = "quit";
        final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";
        final int DEFAULT_SERVER_PORT = 8888;
        Socket socket = null;
        BufferedWriter writer = null;
        try {
            // 1. 创建 Socket
            socket = new Socket(DEFAULT_SERVER_ADDRESS, DEFAULT_SERVER_PORT);
            // 2. 创建 IO 流
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // 3. 阻塞式等待用户输入信息
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                // 读取的是换行符之前的消息
                String input = consoleReader.readLine();
                // 4. 发送消息给服务器
                writer.write(input + "\n");
                writer.flush();
                // 5. 读取服务器返回的消息
                String msg = reader.readLine();
                System.out.println(msg);
                // 查看用户是否退出
                if (QUIT.equals(input)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            if (writer != null) {
                try {
                    writer.close();
                    System.out.println("关闭 socket");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
