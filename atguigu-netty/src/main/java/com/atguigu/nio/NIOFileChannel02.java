package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件拷贝
 *
 * @author miaoqi
 * @date 2020/12/19
 */
public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception {

        //创建文件的输入流
        File file = new File("/Users/miaoqi/Documents/study/language/java/framework/atguigu-netty/file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        //通过fileInputStream 获取对应的FileChannel -> 实际类型  FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将 通道的数据读入到Buffer
        fileChannel.read(byteBuffer);

        //将byteBuffer 的 字节数据 转成String
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();

    }
}
