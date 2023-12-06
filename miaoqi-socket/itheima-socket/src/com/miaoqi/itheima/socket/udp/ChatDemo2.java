package com.miaoqi.itheima.socket.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author miaoqi
 * @date 2017-10-26 下午2:00
 **/
public class ChatDemo2 {

    public static void main(String[] args) {
        new Thread(new ReceTask()).start();
        new Thread(new SendTask()).start();
    }

}

class SendTask implements Runnable {

    @Override
    public void run() {
        DatagramSocket ds = null;
        BufferedReader br = null;
        try {
            ds = new DatagramSocket();
            br = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = br.readLine()) != null) {
                DatagramPacket dp = new DatagramPacket(line.getBytes(), line.getBytes().length, InetAddress
                        .getByName("127.0.0.1"), 12346);
                ds.send(dp);
                if ("886".equals(line)) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ds != null) {
                ds.close();
            }
        }
    }
}

class ReceTask implements Runnable {

    @Override
    public void run() {
        DatagramSocket ds = null;
        byte[] buf = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);
        try {
            ds = new DatagramSocket(12346);
            while (true) {
                ds.receive(dp);
                String content = new String(dp.getData(), 0, dp.getLength());
                if ("886".equals(content)) {
                    break;
                }
                System.out.println(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
    }
}