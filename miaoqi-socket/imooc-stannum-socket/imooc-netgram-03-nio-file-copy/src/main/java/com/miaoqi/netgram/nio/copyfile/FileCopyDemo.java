package com.miaoqi.netgram.nio.copyfile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileCopyDemo {

    private static final int ROUNDS = 5;

    private static void benchmark(FileCopyRunner test, File source, File dest) {
        long elapsed = 0L;
        for (int i = 0; i < ROUNDS; i++) {
            long startTime = System.currentTimeMillis();
            test.copy(source, dest);
            elapsed += System.currentTimeMillis() - startTime;
            dest.delete();
        }
        System.out.println(test + ": " + elapsed / ROUNDS);
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // 最传统的没有缓冲区的流, 一个 byte 一个 byte 的读取源数据
        FileCopyRunner noBufferStreamCopy = new FileCopyRunner() {
            @Override
            public void copy(File source, File dest) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = new FileInputStream(source);
                    out = new FileOutputStream(dest);
                    int result;
                    while ((result = in.read()) != -1) {
                        out.write(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(in);
                    close(out);
                }
            }

            @Override
            public String toString() {
                return "noBufferStreamCopy";
            }
        };
        // 使用带缓冲区的传统流, 一次读取多个 byte 数据到缓冲区, 我们再从缓冲区读取数据
        FileCopyRunner bufferedStreamCopy = new FileCopyRunner() {
            @Override
            public void copy(File source, File dest) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = new BufferedInputStream(new FileInputStream(source));
                    out = new BufferedOutputStream(new FileOutputStream(dest));

                    byte[] buffer = new byte[1024];
                    int result;
                    while ((result = in.read(buffer)) != -1) {
                        out.write(buffer, 0, result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(in);
                    close(out);
                }
            }

            @Override
            public String toString() {
                return "bufferedStreamCopy";
            }
        };
        // 使用 nio 的 buffer 读取数据
        FileCopyRunner nioBufferCopy = new FileCopyRunner() {
            @Override
            public void copy(File source, File dest) {
                FileChannel in = null;
                FileChannel out = null;
                try {
                    in = new FileInputStream(source).getChannel();
                    out = new FileOutputStream(dest).getChannel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    while (in.read(buffer) != -1) {
                        // 写模式转为读模式
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            out.write(buffer);
                        }
                        // 读模式转为写模式
                        buffer.clear();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(in);
                    close(out);
                }
            }

            @Override
            public String toString() {
                return "nioBufferCopy";
            }
        };
        // 使用 nio 的 transfer 在两个 channel 之间传输数据, 没有使用 Buffer
        FileCopyRunner nioTransferCopy = new FileCopyRunner() {
            @Override
            public void copy(File source, File dest) {
                FileChannel in = null;
                FileChannel out = null;
                try {
                    in = new FileInputStream(source).getChannel();
                    out = new FileOutputStream(dest).getChannel();
                    long transferred = 0L;
                    long size = in.size();
                    while (transferred != size) {
                        // 累计每次拷贝的数量
                        transferred += in.transferTo(0, size, out);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(in);
                    close(out);
                }
            }

            @Override
            public String toString() {
                return "nioTransferCopy";
            }
        };
    }


}
