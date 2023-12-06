package com.miaoqi.itheima.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author miaoqi
 * @date 2017-10-18 下午6:22
 **/
public class Receive implements Runnable {

    private DatagramSocket ds;

    public Receive(DatagramSocket ds) {
        this.ds = ds;
    }

    @Override
    public void run() {
        while (true) {
            byte[] buf = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            try {
                ds.receive(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String content = new String(dp.getData(), 0, dp.getLength());
            System.out.println("content: " + content);
            if ("886".equals(content)) {
                break;
            }
        }
    }
}
