package com.miaoqi.itheima.socket.tcp;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author miaoqi
 * @date 2017-12-28 下午8:53
 **/
public class MySendDemo {
    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("localhost", 12345);

        OutputStream out = clientSocket.getOutputStream();
        out.write("你好吗".getBytes());
        clientSocket.shutdownOutput();
        InputStream in = clientSocket.getInputStream();
        byte[] buff = new byte[1024];
        int len = -1;
        while ((len = in.read(buff)) != -1) {
            System.out.println(new String(buff, 0, len));
        }
        in.close();
        out.close();
        clientSocket.close();
    }
}
