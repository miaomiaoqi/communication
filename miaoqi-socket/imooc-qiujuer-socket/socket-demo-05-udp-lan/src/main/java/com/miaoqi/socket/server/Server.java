package com.miaoqi.socket.server;


import com.miaoqi.socket.constants.TCPConstants;

import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        ServerProvider.start(TCPConstants.PORT_SERVER);

        try {
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServerProvider.stop();
    }
}
