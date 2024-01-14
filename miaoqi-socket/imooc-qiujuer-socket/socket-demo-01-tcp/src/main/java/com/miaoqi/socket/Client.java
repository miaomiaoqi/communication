package com.miaoqi.socket;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(); 
        // 设置读取的超时时间
        socket.setSoTimeout(3000);
        // 连接本地, 端口2000, 连接超时时间3000
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 2000), 3000);
        System.out.println("已发起服务器连接, 并进入后续流程~");
        System.out.println("客户端信息: " + socket.getLocalAddress() + " P: " + socket.getLocalPort());
        System.out.println("服务端信息: " + socket.getInetAddress() + " P: " + socket.getPort());

        try {
            // 发送接收数据
            todo(socket);
        } catch (Exception e) {
            System.out.println("异常关闭");
        }

        // 释放资源
        socket.close();
        System.out.println("客户端已退出~");
    }

    private static void todo(Socket client) throws Exception {
        // 构建键盘输入流
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        // 得到socket输出流, 转换为打印流
        OutputStream out = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(out);

        // 得到socket输入流, 并转换为字符缓冲流
        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        boolean flag = true;
        do {
            // 从键盘读取一行
            String line = input.readLine();
            // 发送到服务器
            socketPrintStream.println(line);

            // 从服务器读取一行
            String echo = socketBufferedReader.readLine();
            if ("bye".equals(echo)) {
                flag = false;
            } else {
                System.out.println(echo);
            }
        } while (flag);

        // 资源释放
        socketPrintStream.close();
        socketBufferedReader.close();
    }

}
