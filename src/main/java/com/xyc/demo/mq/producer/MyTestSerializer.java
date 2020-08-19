package com.xyc.demo.mq.producer;

import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class MyTestSerializer implements Serializer<Object> {
    @Override
    public void configure(Map configs, boolean isKey) {
        //初始化时候调用一次，配置这个对象，一般用来确定编码
    }

    @Override
    public byte[] serialize(String topic, Object data) {
        //具体的序列化方法，可根据topic采用不同的序列化方法，默认直接String的getByte方法
        return new byte[0];
    }

    @Override
    public void close() {
        //关闭资源，一般为null，实现需要注意幂等性
    }
}
