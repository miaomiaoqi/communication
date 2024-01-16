package com.atguigu.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 1. MapperByteBuffer 可以直接让文件在内存(堆外内存)修改, 操作系统不需要拷贝一次
 *
 * @author miaoqi
 * @date 2024-01-16 11:22:10
 */
public class MapperByteBuffer {

    public static void main(String[] args) throws Exception {
        RandomAccessFile rw = new RandomAccessFile("/Users/miaoqi/Documents/study/language/java/communication/atguigu-netty/1.txt", "rw");
        FileChannel channel = rw.getChannel();

        // 参数 1: FileChannel.MapMode.READ_WRITE 使用的读写模式
        // 参数 2: 0 可以直接修改的起始位置
        // 参数 3: 5 是映射到内存的大小, 即将 1.txt 的多少个字节映射到内存
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');

        rw.close();
    }

}
