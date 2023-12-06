package com.miaoqi.itheima.socket.upload;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author miaoqi
 * @date 2017-10-20 下午1:53
 **/
public class UploadTxtServer {
    public static void main(String[] args) throws IOException {
        System.out.println("上传文本文件服务端启动中...");
        ServerSocket ss = new ServerSocket(12346);
        Socket s = ss.accept();

        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter pw = new PrintWriter(new FileWriter("/Users/miaoqi/Documents/server.txt"), true);
        String line;
        while ((line = br.readLine()) != null) {
            pw.println(line);
        }

        pw.close();
        br.close();
        ss.close();
    }
}
