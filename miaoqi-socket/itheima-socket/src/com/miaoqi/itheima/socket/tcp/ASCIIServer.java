package com.miaoqi.itheima.socket.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author miaoqi
 * @date 2017-11-12 下午6:10
 **/
public class ASCIIServer {

    public static void main(String[] args) throws Exception {
        // 1. 创建服务端socket
        ServerSocket ss = new ServerSocket(8080);
        // 2. 接收客户端
        while (true) {
            Socket socket = ss.accept();
            // 3. 开启线程通信, 实现了第一个question
            new Thread(new ServerTask(socket)).start();
        }
    }
}

// 服务端任务
class ServerTask implements Runnable {

    private Socket socket;

    public ServerTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            // 1. 获取输入流
            InputStream in = socket.getInputStream();
            // 2. 获取输出流
            OutputStream out = socket.getOutputStream();
            // 3. 接收客户端数据
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = in.read()) != -1) {
                sb.append((char) ch);
            }
            out.write(sb.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 3. 关闭socket
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
