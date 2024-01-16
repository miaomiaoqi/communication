package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {

    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("/Users/miaoqi/Documents/study/language/java/communication/atguigu-netty/Koala.jpg");
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream(
                "/Users/miaoqi/Documents/study/language/java/communication/atguigu-netty/Koala2.jpg");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        // 使用 transferFrom 完成拷贝
        fileOutputStreamChannel.transferFrom(fileInputStreamChannel, 0, fileInputStreamChannel.size());

        // 关闭相关的流
        fileInputStream.close();
        fileOutputStream.close();
    }

}
