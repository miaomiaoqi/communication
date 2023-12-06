package com.miaoqi.netgram.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * BIO 聊天室, 接收用户输入的信息并发送给服务端
 *
 * @author miaoqi
 * @date 2019/11/26
 */
public class UserInputHandler implements Runnable {

    private ChatClient chatClient;

    public UserInputHandler(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void run() {
        try {
            // 等待用户输入消息
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String input = reader.readLine();
                // 向服务器发送消息
                this.chatClient.send(input);
                if (this.chatClient.readyToQuit(input)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
