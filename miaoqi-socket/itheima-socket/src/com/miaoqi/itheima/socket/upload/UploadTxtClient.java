package com.miaoqi.itheima.socket.upload;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author miaoqi
 * @date 2017-10-20 下午1:53
 **/
public class UploadTxtClient {
    public static void main(String[] args) throws IOException {
        // /Users/miaoqi/Documents/client.txt
        System.out.println("上传文本文件服务端客户中...");
        Socket s = new Socket("127.0.0.1", 12346);
        BufferedReader br = new BufferedReader(new FileReader("/Users/miaoqi/Documents/client.txt"));
        OutputStream out = s.getOutputStream();
        PrintWriter pw = new PrintWriter(out, true);
        String line;
        while ((line = br.readLine()) != null) {
            pw.println(line);
        }
        br.close();
        pw.close();
        s.close();
    }
}
