package com.miaoqi.itheima.socket.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author miaoqi
 * @date 2017-10-18 上午11:04
 **/
public class SendDemo1 {
    public static void main(String[] args) throws Exception {
        System.out.println("启动客户端...");
        DatagramSocket ds = new DatagramSocket();

        String text = "UDP 我来了!";
        DatagramPacket dp = new DatagramPacket(text.getBytes(), text.getBytes().length);
        dp.setAddress(InetAddress.getByName("127.0.0.1"));
        dp.setPort(12345);

        ds.send(dp);

        ds.close();
    }
}
