package com.miaoqi.webserver.processor;

import com.miaoqi.webserver.connector.Request;
import com.miaoqi.webserver.connector.Response;

import java.io.IOException;

public class StaticProcessor {

    public void process(Request request, Response response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
