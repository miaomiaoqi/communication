package com.miaoqi.socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * UDP搜索者, 用于搜索服务支持方
 *
 * @author miaoqi
 * @date 2019-03-17
 */
public class UDPSearcher {

    private static final int LISTEN_PORT = 30000;

    public static void main(String[] args) throws Exception {
        System.out.println("UDPSearcher Started.");

        Listener listener = listen();
        sendBroadcast();

        // 读取任意键盘输入后可退出
        System.in.read();
        List<Device> devices = listener.getDevicesAndClose();
        for (Device device : devices) {
            System.out.println("Deivce: " + device);
        }

        // 完成
        System.out.println("UDPSearcher Finished.");
    }

    /**
     * 广播发送
     *
     * @author miaoqi
     * @date 2019-07-03
     * @param
     * @return
     */
    private static void sendBroadcast() throws Exception {
        System.out.println("UDPSearcher sendBroadcast Started.");
        // 作为发送者, 让系统自动分配端口
        DatagramSocket ds = new DatagramSocket();

        // 构建一份请求数据
        String requestData = MessageCreator.buildWithPort(LISTEN_PORT);
        byte[] requestDataBytes = requestData.getBytes();
        // 直接根据发送者构建一份回送信息
        DatagramPacket requestPack = new DatagramPacket(requestDataBytes, requestDataBytes.length);
        // 受限广播地址(局域网内收到)
        requestPack.setAddress(Inet4Address.getByName("255.255.255.255"));
        requestPack.setPort(20000);

        // 发送
        ds.send(requestPack);
        ds.close();

        // 完成
        System.out.println("UDPSearcher sendBroadcast Finished.");
    }

    /**
     * 构建监听器
     *
     * @author miaoqi
     * @date 2019-07-03
     * @param
     * @return
     */
    private static Listener listen() throws InterruptedException {
        System.out.println("UDPSearcher start listen.");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Listener listener = new Listener(LISTEN_PORT, countDownLatch);
        listener.start();

        countDownLatch.await();
        return listener;
    }

    /**
     * 封装 Provider 数据
     *
     * @author miaoqi
     * @date 2019-07-03
     */
    private static class Device {
        final int port;
        final String ip;
        final String sn;

        public Device(int port, String ip, String sn) {
            this.port = port;
            this.ip = ip;
            this.sn = sn;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Device{");
            sb.append("port=").append(port);
            sb.append(", ip='").append(ip).append('\'');
            sb.append(", sn='").append(sn).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * 监听 Provider 回调
     *
     * @author miaoqi
     * @date 2019-07-03
     */
    private static class Listener extends Thread {

        private final int listenPort;
        private final CountDownLatch countDownLatch;
        private final List<Device> devices = new ArrayList<Device>();
        private boolean done = false;
        private DatagramSocket ds = null;

        public Listener(int listenPort, CountDownLatch countDownLatch) {
            this.listenPort = listenPort;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            super.run();
            // 通知已启动
            this.countDownLatch.countDown();
            try {
                // 监听回送端口
                ds = new DatagramSocket(listenPort);
                while (!done) {
                    // 构建接收实体
                    final byte[] buf = new byte[512];
                    DatagramPacket receivePack = new DatagramPacket(buf, buf.length);
                    // 接收, 阻塞
                    ds.receive(receivePack);
                    // 打印接收到的信息与发送者的信息
                    // 发送者的IP
                    String ip = receivePack.getAddress().getHostAddress();
                    int port = receivePack.getPort();
                    int dataLen = receivePack.getLength();
                    String data = new String(receivePack.getData(), 0, dataLen);
                    System.out.println("UDPSearcher receive from ip: " + ip + "\tport: " + port + "\tdata: " + data);

                    String sn = MessageCreator.parseSn(data);
                    if (sn != null) {
                        Device device = new Device(port, ip, data);
                        devices.add(device);
                    }
                }
            } catch (Exception e) {

            } finally {
                close();
            }
            System.out.println("UDPSearcher Listener Finished.");
        }

        private void close() {
            if (ds != null) {
                ds.close();
                ds = null;
            }
        }

        List<Device> getDevicesAndClose() {
            done = true;
            close();
            return devices;
        }
    }
}
