package com.miaoqi.itheima.socket.udp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author miaoqi
 * @date 2017-10-18 上午11:04
 **/
public class SendDemo2 {
    public static void main(String[] args) throws Exception {
        System.out.println("发送端启动...");
        DatagramSocket ds = new DatagramSocket(12343);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = br.readLine()) != null) {
            DatagramPacket dp = new DatagramPacket(line.getBytes(), line.getBytes().length, InetAddress.getByName
                    ("127.0.0.1"), 12344);
            ds.send(dp);
            if ("886".equals(line)) {
                break;
            }
        }
        ds.close();
        System.out.println("发送端停止...");
    }
}
