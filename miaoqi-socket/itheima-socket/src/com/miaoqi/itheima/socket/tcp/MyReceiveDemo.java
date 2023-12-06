package com.miaoqi.itheima.socket.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author miaoqi
 * @date 2017-12-28 下午8:57
 **/
public class MyReceiveDemo {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        Socket socket = serverSocket.accept();
        InputStream in = socket.getInputStream();
        byte[] buff = new byte[1024];
        int len = -1;
        while ((len = in.read(buff)) != -1) {
            System.out.println(new String(buff, 0, len));
        }
        OutputStream out = socket.getOutputStream();
        out.write("我很好".getBytes());
        out.close();
        in.close();
        serverSocket.close();
    }
}
