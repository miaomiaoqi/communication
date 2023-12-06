package net.qiujuer.lesson.sample.client;


import net.qiujuer.lesson.sample.client.bean.ServerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Client {
    public static void main(String[] args) {
        ServerInfo info = UDPSearcher.searchServer(10000);
        System.out.println("Server:" + info);

        if (info != null) {
            TCPClient client = null;
            try {
                client = TCPClient.startWith(info);
                if (client == null) {
                    return;
                }
                write(client);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (client != null) {
                    client.exit();
                }
            }
        }
    }

    private static void write(TCPClient client) throws IOException {
        // 构建键盘输入流
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        do {
            // 键盘读取一行
            String msg = input.readLine();
            // 发送到服务器
            client.send(msg);
            if ("00bye00".equalsIgnoreCase(msg)) {
                break;
            }
        } while (true);
    }
}
