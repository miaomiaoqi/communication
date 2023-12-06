package com.miaoqi.itheima.socket.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author miaoqi
 * @date 2017-12-28 下午8:09
 **/
public class MySendDemo {

    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        while ((line = reader.readLine()) != null) {
            DatagramPacket dp = new DatagramPacket(line.getBytes(), line.getBytes().length, InetAddress.getByName
                    ("127.0.0.1"), 12345);
            ds.send(dp);
            if ("886".equalsIgnoreCase(line)) {
                break;
            }
        }
        ds.close();
    }

}
