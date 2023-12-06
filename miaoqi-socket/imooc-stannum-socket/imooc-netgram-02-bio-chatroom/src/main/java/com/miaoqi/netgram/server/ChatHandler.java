package com.miaoqi.netgram.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 负责转发每个客户端发来的消息
 *
 * @author miaoqi
 * @date 2019/11/26
 */
public class ChatHandler implements Runnable {

    private ChatServer chatServer;
    private Socket socket;

    public ChatHandler(ChatServer chatServer, Socket socket) {
        this.chatServer = chatServer;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // 存储新上线用户
            this.chatServer.addClient(socket);
            // 读取用户发送的消息
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            // 如果客户端关闭了 socket, 那么会读到一个 null
            String msg;
            while ((msg = reader.readLine()) != null) {
                String fwdMsg = "客户端[" + socket.getPort() + "]: " + msg + "\n";
                System.out.print(fwdMsg);
                // 转发给聊天室中的其他用户
                this.chatServer.forwardMessage(socket, fwdMsg);
                if (this.chatServer.readyToQuit(msg)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.chatServer.removeClient(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
