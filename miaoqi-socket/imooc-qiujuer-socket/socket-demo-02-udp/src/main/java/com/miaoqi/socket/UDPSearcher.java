package com.miaoqi.socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;

/**
 * UDP搜索者, 用于搜索服务支持方
 *
 * @author miaoqi
 * @date 2019-03-17
 */
public class UDPSearcher {

    public static void main(String[] args) throws Exception {
        System.out.println("UDPSearcher Started.");
        // 作为发送者, 让系统自动分配端口
        DatagramSocket ds = new DatagramSocket();

        // 构建一份请求数据
        String requestData = "HelloWorld!";
        byte[] requestDataBytes = requestData.getBytes();
        // 直接根据发送者构建一份回送信息
        DatagramPacket requestPack = new DatagramPacket(requestDataBytes, requestDataBytes.length);
        requestPack.setAddress(Inet4Address.getLocalHost());
        requestPack.setPort(20000);

        // 发送
        ds.send(requestPack);

        // 构建接收实体
        final byte[] buf = new byte[512];
        DatagramPacket receivePack = new DatagramPacket(buf, buf.length);
        // 接收
        ds.receive(receivePack);
        // 打印接收到的信息与发送者的信息
        // 发送者的IP
        String ip = receivePack.getAddress().getHostAddress();
        int port = receivePack.getPort();
        int dataLen = receivePack.getLength();
        String data = new String(receivePack.getData(), 0, dataLen);
        System.out.println("UDPSearcher receive from ip: " + ip + "\tport: " + port + "\tdata: " + data);


        // 完成
        System.out.println("UDPSearcher Finished.");
        ds.close();
    }

}
