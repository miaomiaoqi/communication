package com.miaoqi.itheima.socket.tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author miaoqi
 * @date 2017-11-12 下午6:18
 **/
public class ASCIIClient {

    public static void main(String[] args) throws Exception {
        Socket s = new Socket("127.0.0.1", 8080);
        OutputStream out = s.getOutputStream();
        InputStream in = s.getInputStream();
        for (int i = 97; i <= 122; i++) {
            out.write(i);
        }
        s.shutdownOutput();
        byte[] buf = new byte[1024];
        int len = in.read(buf);
        System.out.println(new String(buf, 0, len));
        s.close();
    }

}
