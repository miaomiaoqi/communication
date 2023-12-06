package com.miaoqi.itheima.socket.udp;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author miaoqi
 * @date 2017-10-18 下午6:21
 **/
public class ChatDemo {
    public static void main(String[] args) throws SocketException {
        new Thread(new Send(new DatagramSocket())).start();
        new Thread(new Receive(new DatagramSocket(12344))).start();
    }
}
