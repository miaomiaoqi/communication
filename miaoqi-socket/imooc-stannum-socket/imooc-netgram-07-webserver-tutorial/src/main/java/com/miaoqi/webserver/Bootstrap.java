package com.miaoqi.webserver;

import com.miaoqi.webserver.connector.Connector;

public class Bootstrap {

    public static void main(String[] args) {
        Connector connector = new Connector();
        connector.start();
    }

}
