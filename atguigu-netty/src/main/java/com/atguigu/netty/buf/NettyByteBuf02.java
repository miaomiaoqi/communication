package com.atguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyByteBuf02 {

    public static void main(String[] args) {
        // 创建 ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello, world!", CharsetUtil.UTF_8);
        // 使用相关的 API
        if (byteBuf.hasArray()) { // true
            byte[] content = byteBuf.array();
            System.out.println(new String(content, Charset.forName("UTF-8")));
            System.out.println("bytebuf = " + byteBuf);
            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());

            int len = byteBuf.readableBytes(); // 可读取的字节数
            System.out.println("len = " + len);
        }
    }

}
