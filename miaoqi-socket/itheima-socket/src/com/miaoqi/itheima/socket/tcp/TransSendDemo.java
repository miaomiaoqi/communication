package com.miaoqi.itheima.socket.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author miaoqi
 * @date 2017-10-18 下午6:38
 **/
public class TransSendDemo {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("127.0.0.1", 12344);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        OutputStream out = s.getOutputStream();
        InputStream in = s.getInputStream();
        String line;
        while ((line = br.readLine()) != null) {
            if ("over".equals(line)) {
                break;
            }
            out.write(line.getBytes());
            byte[] buf = new byte[1024];
            int len = in.read(buf);
            System.out.println(new String(buf, 0, len));
        }
        s.close();
    }
}
