package com.miaoqi.socket.server.handle;

import com.miaoqi.socket.clink.utils.CloseUtils;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {

    private Socket socket;
    private final ClientReadHandler readHandler;
    private final ClientSendHandler sendHandler;
    private final CloseNotify closeNotify;

    public ClientHandler(Socket socket, CloseNotify closeNotify) throws IOException {
        this.socket = socket;
        this.readHandler = new ClientReadHandler(socket.getInputStream());
        this.sendHandler = new ClientSendHandler(socket.getOutputStream());
        this.closeNotify = closeNotify;
        System.out.println("新客户端连接：" + socket.getInetAddress() +
                " P:" + socket.getPort());
    }

    public void exit() {
        readHandler.exit();
        sendHandler.exit();
        CloseUtils.close(socket);
        System.out.println("客户端已退出: " + socket.getInetAddress() + " P: " + socket.getPort());
    }

    public void send(String str) {
        this.sendHandler.send(str);
    }

    public void readToPrint() {
        this.readHandler.start();
    }

    private void exitBySelf() {
        this.exit();
        this.closeNotify.onSelfClosed(this);
    }

    public interface CloseNotify {
        void onSelfClosed(ClientHandler clientHandler);
    }

    /**
     * 客户端接收处理器
     *
     * @author miaoqi
     * @date 2019-04-04
     */
    class ClientReadHandler extends Thread {
        private boolean done = false;
        private final InputStream inputStream;

        ClientReadHandler(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            super.run();
            try {
                // 得到输入流，用于接收数据
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(inputStream));
                do {
                    // 从客户端拿到一条数据
                    String str = socketInput.readLine();
                    if (str == null) {
                        System.out.println("客户端已无法读取数据");
                        // 退出当前客户端
                        ClientHandler.this.exitBySelf();
                        break;
                    }
                    // 打印到屏幕
                    System.out.println(str);
                } while (!done);
            } catch (Exception e) {
                if (!done) {
                    System.out.println("连接异常断开");
                    ClientHandler.this.exitBySelf();
                }
            } finally {
                // 连接关闭
                CloseUtils.close(inputStream);
            }
        }

        void exit() {
            done = true;
            CloseUtils.close(inputStream);
        }
    }

    /**
     * 客户端发送处理器
     *
     * @author miaoqi
     * @date 2019-04-04
     */
    class ClientSendHandler {

        private boolean done = false;
        private final PrintStream printStream;
        private final ExecutorService executorService;

        ClientSendHandler(OutputStream outputStream) {
            this.printStream = new PrintStream(outputStream);
            this.executorService = Executors.newSingleThreadExecutor();
        }

        void exit() {
            done = true;
            CloseUtils.close(printStream);
            this.executorService.shutdown();
        }

        void send(String str) {
            this.executorService.execute(new SendRunnable(str));
        }

        /**
         * 创建实现类, 方便传参
         *
         * @author miaoqi
         * @date 2019-04-04
         */
        class SendRunnable implements Runnable {

            private final String msg;

            SendRunnable(String msg) {
                this.msg = msg;
            }

            @Override
            public void run() {
                if (ClientSendHandler.this.done) {
                    return;
                }
                try {
                    ClientSendHandler.this.printStream.println(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
