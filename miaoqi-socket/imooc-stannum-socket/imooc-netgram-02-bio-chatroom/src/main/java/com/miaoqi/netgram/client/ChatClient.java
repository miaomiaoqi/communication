package com.miaoqi.netgram.client;

import java.io.*;
import java.net.Socket;

/**
 * BIO 聊天室客户端, 负责建立连接, 接收服务器消息
 *
 * @author miaoqi
 * @date 2019/11/26
 */
public class ChatClient {

    private final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";
    private final int DEFAULT_SERVER_PORT = 8888;
    private final String QUIT = "quit";

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public ChatClient() {

    }

    // 发送消息给服务器
    public void send(String msg) throws IOException {
        if (!this.socket.isOutputShutdown()) {
            writer.write(msg + "\n");
            writer.flush();
        }
    }

    // 从服务器接收消息
    public String receive() throws IOException {
        String msg = null;
        if (!this.socket.isInputShutdown()) {
            msg = this.reader.readLine();
        }
        return msg;
    }

    // 检查用户是否准备退出
    public boolean readyToQuit(String msg) {
        return QUIT.equalsIgnoreCase(msg);
    }

    public synchronized void close() {
        if (this.writer != null) {
            try {
                System.out.println("关闭 socket");
                this.writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        try {
            // 创建 socket
            socket = new Socket(DEFAULT_SERVER_ADDRESS, DEFAULT_SERVER_PORT);
            // 创建 IO 流
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            // 处理用户的输入
            new Thread(new UserInputHandler(this)).start();
            // 读取服务器转发的消息
            String msg;
            while ((msg = receive()) != null) {
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.start();
    }

}
