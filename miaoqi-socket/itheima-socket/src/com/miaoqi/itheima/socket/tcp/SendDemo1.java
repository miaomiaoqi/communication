package com.miaoqi.itheima.socket.tcp;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author miaoqi
 * @date 2017-10-18 下午5:20
 **/
public class SendDemo1 {
    public static void main(String[] args) throws IOException {
        System.out.println("TCP客户端启动...");
        Socket s = new Socket("127.0.0.1", 12346);
        OutputStream out = s.getOutputStream();
        out.write("你好吗".getBytes());
        InputStream in = s.getInputStream();
        byte[] buf = new byte[1024];
        int len = in.read(buf);
        System.out.println(new String(buf, 0, len));
        s.close();
        System.out.println("TCP客户端终止...");
    }
}
