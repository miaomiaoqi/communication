package com.miaoqi.itheima.socket.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author miaoqi
 * @date 2017-10-26 下午2:27
 **/
public class ChatDemo {
    public static void main(String[] args) {
        new Thread(new ReceTask()).start();
        new Thread(new SendTask()).start();
    }
}

class ReceTask implements Runnable {

    @Override
    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(12346);
            Socket s = ss.accept();
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if ("886".equals(line)) {
                    break;
                }
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class SendTask implements Runnable {

    @Override
    public void run() {
        try {
            Socket s = new Socket("127.0.0.1", 12346);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
                bw.flush();
                if ("886".equals(line)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
