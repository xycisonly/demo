package com.xyc.demo.mq.consumer;

import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class MyTestDeserializer implements Deserializer<String> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //初始化
    }

    @Override
    public String deserialize(String topic, byte[] data) {
        return null;//反序列化
    }

    @Override
    public void close() {
        //关闭资源
    }
}
