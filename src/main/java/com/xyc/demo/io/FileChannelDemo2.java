package com.xyc.demo.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * https://blog.csdn.net/m0_51001708/article/details/120243503
 *
 * @author xiaoyuchen
 * @date 2022/5/2
 */
public class FileChannelDemo2 {
    public static void main(String[] args) throws IOException {
        //打开FileChannel
        RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\gwt.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();

        //创建buffer对象
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String newData = "Hello";

        //写入内容
        buffer.put(newData.getBytes());
        buffer.flip();

        //FileChannel最终实现
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }

        //关闭
        channel.close();
    }

    public static void main2(String[] args) throws IOException {
        //sendFile
        RandomAccessFile randomAccessFile1 = new RandomAccessFile("D:\\gwt.txt", "rw");
        RandomAccessFile randomAccessFile2 = new RandomAccessFile("D:\\qwe.txt", "rw");
        FileChannel fromChannel = randomAccessFile1.getChannel();
        FileChannel toChannel = randomAccessFile2.getChannel();
        long position = 0;
        long count = fromChannel.size();
        fromChannel.transferTo(position, count, toChannel);
        //toChannel.transferFrom() 不是sendfile 更像是mmap https://www.jianshu.com/p/713af3a13bde
        randomAccessFile1.close();
        randomAccessFile2.close();
        System.out.println("结束");
    }

    public static void main4(String[] args) throws IOException {
        //mmap
        FileChannel channel = new RandomAccessFile("D://data.txt", "r")
                .getChannel();
        //MappedByteBuffer 便是MMAP的操作类(映射一个 1.5G 的文件)
        //MapMode.READ_ONLY：只读，试图修改得到的缓冲区将导致抛出异常。
        //MapMode.READ_WRITE：读/写，对得到的缓冲区的更改最终将写入文件；但该更改对映射到同一文件的其他程序不一定是可见的。
        //MapMode.PRIVATE：私用，可读可写,但是修改的内容不会写入文件，只是buffer自身的改变，这种能力称之为”copy on write”。
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1 * 1024 * 1024 * 1024);

        // write
        byte[] data1 = new byte[4];
        //从当前 mmap 指针的位置写入 4b 的数据
        mappedByteBuffer.put(data1);
        //指定 position 写入 4b 的数据
        mappedByteBuffer.position(8);
        mappedByteBuffer.put(data1);

        // read
        byte[] data2 = new byte[4];
        //从当前 mmap 指针的位置读取 4b 的数据
        mappedByteBuffer.get(data2);
        //指定 position 读取 4b 的数据
        mappedByteBuffer.position(8);
        mappedByteBuffer.get(data2);
        channel.close();

        //显然mmap 只进行了一半的"代理"，所以比sendfile多一次拷贝
    }
}