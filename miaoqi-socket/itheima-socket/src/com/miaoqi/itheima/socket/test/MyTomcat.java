package com.miaoqi.itheima.socket.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author miaoqi
 * @date 2017-10-20 下午3:23
 **/
public class MyTomcat {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(12346);
        Socket s = ss.accept();

        InputStream in = s.getInputStream();
        byte[] buf = new byte[1024];
        int len = in.read(buf);
        System.out.println(new String(buf, 0, len));

        OutputStream out = s.getOutputStream();
        out.write("<font color='red' size='7'>哈哈哈</font>".getBytes());

        out.close();
        ss.close();
    }
}
