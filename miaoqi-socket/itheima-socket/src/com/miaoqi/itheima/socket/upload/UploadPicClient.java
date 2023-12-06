package com.miaoqi.itheima.socket.upload;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author miaoqi
 * @date 2017-10-20 下午2:22
 **/
public class UploadPicClient {
    public static void main(String[] args) throws IOException {
        // /Users/miaoqi/Documents/client.png
        Socket s = new Socket("127.0.0.1", 12346);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("/Users/miaoqi/Documents/client.png"));
        OutputStream out = s.getOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = bis.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        s.shutdownOutput();

        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
        bis.close();
        s.close();

    }
}
