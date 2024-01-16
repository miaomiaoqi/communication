package com.atguigu.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {

    public static void main(String[] args) throws Exception {
        String str = "hello, 尚硅谷";
        // 创建一个输出流 -> channel
        FileOutputStream fileOutputStream = new FileOutputStream(
                "/Users/miaoqi/Documents/study/language/java/communication/atguigu-netty/file01.txt");
        // 通过 fileoutputstream 获取对应的 FileChannel
        // 这个 fileChannel 的真实类型是 FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
        // 创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 将 str 放入 byteBuffer
        byteBuffer.put(str.getBytes());
        // 对 byteBuffer 进行反转
        byteBuffer.flip();
        // 将 byteBuffer 的数据写入到 fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }

}
