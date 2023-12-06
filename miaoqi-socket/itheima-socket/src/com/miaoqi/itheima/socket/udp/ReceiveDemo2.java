package com.miaoqi.itheima.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author miaoqi
 * @date 2017-10-18 上午11:04
 **/
public class ReceiveDemo2 {
    public static void main(String[] args) throws IOException {
        System.out.println("接收端启动...");
        DatagramSocket ds = new DatagramSocket(12344);
        byte[] buf = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);

        while (true) {
            ds.receive(dp);
            String ip = dp.getAddress().getHostAddress();
            String content = new String(dp.getData(), 0, dp.getLength());
            System.out.println("ip: " + ip + " content: " + content);
            if ("886".equals(content)) {
                break;
            }
        }
        ds.close();
        System.out.println("接收端停止...");
    }
}
