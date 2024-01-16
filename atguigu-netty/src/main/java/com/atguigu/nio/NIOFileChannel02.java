package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {

    public static void main(String[] args) throws Exception {

        // 创建文件的输入流
        File file = new File("/Users/miaoqi/Documents/study/language/java/communication/atguigu-netty/file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        // 通过 fileInputStream 获取对应的 FileChannel -> 实际类型 FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();
        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        // 将通道的数据读入到 Buffer 中
        fileChannel.read(byteBuffer);
        // 将 ByteBuffer 的字节数据转成 String
        System.out.println(new String(byteBuffer.array()));
        // 将 byteBuffer 的数据写入到 fileChannel
        fileInputStream.close();
    }

}
