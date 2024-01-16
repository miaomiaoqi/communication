package com.atguigu.nio;

import java.nio.ByteBuffer;

public class NIOByteBufferPutGet {

    public static void main(String[] args) {
        // 创建一个 buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        // 类型化方式放入数据
        byteBuffer.putInt(100);
        byteBuffer.putLong(9);
        byteBuffer.putChar('苗');
        byteBuffer.putShort((short) 4);

        // 取出
        byteBuffer.flip();

        System.out.println();

        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getShort());
    }

}
