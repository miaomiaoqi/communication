package com.miaoqi.itheima.socket.udp;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author miaoqi
 * @date 2017-12-28 下午8:13
 **/
public class MyReceiveDemo {

    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket(12345);
        DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
        while (true) {
            ds.receive(dp);
            String addr = dp.getAddress().getHostName();
            String data = new String(dp.getData(), 0, dp.getLength());
            if ("886".equalsIgnoreCase(data)) {
                break;
            }
            System.out.println("ip: " + addr + " data:" + data);
        }
        ds.close();
    }

}
