package com.miaoqi.itheima.socket.upload;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author miaoqi
 * @date 2017-10-20 下午2:23
 **/
public class UploadPicServer {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(12346);
        Socket s = ss.accept();
        InputStream in = s.getInputStream();
        FileOutputStream fos = new FileOutputStream("/Users/miaoqi/Documents/server.png");
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) {
            fos.write(buf, 0, len);
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        bw.write("上传成功");
        bw.close();
        fos.close();
        ss.close();

    }
}
