package com.miaoqi.itheima.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author miaoqi
 * @date 2017-10-18 上午11:04
 **/
public class ReceiveDemo1 {
    public static void main(String[] args) throws IOException {
        System.out.println("启动服务端...");
        DatagramSocket ds = new DatagramSocket(12345);
        byte[] buf = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);
        ds.receive(dp);

        String ip = dp.getAddress().getHostAddress();
        String text = new String(dp.getData(), 0, dp.getLength());
        System.out.println("ip: " + ip + " text: " + text);

        ds.close();
    }
}
