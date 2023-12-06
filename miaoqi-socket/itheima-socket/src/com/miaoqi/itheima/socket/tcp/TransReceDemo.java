package com.miaoqi.itheima.socket.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author miaoqi
 * @date 2017-10-18 下午6:39
 **/
public class TransReceDemo {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(12344);
        Socket s = ss.accept();
        InputStream in = s.getInputStream();
        OutputStream out = s.getOutputStream();
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = in.read(buf)) != -1) {
            String content = new String(buf, 0, len);
            if ("over".equalsIgnoreCase(content)) {
                break;
            }
            out.write(content.toUpperCase().getBytes());
        }
        s.close();
        ss.close();
    }
}
