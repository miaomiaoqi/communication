package com.miaoqi.itheima.socket.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author miaoqi
 * @date 2017-10-18 下午6:22
 **/
public class Send implements Runnable {

    private DatagramSocket ds;

    public Send(DatagramSocket ds) {
        this.ds = ds;
    }

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                byte[] buf = line.getBytes();
                DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName("127.0.0.1"), 12344);
                ds.send(dp);
                if ("886".equals(line)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
