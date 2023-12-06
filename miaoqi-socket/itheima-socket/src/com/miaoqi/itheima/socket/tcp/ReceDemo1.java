package com.miaoqi.itheima.socket.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author miaoqi
 * @date 2017-10-18 下午5:21
 **/
public class ReceDemo1 {
    public static void main(String[] args) throws IOException {
        System.out.println("TCP服务端启动...");
        ServerSocket ss = new ServerSocket(12346);
        Socket s = ss.accept();
        InputStream in = s.getInputStream();
        OutputStream out = s.getOutputStream();

        byte[] buf = new byte[1024];
        int len = in.read(buf);
        System.out.println(new String(buf, 0, len));
        out.write("我很好".getBytes());
        s.close();
        ss.close();
        System.out.println("TCP服务端终止...");
    }
}
