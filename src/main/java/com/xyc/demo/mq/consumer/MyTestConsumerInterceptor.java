package com.xyc.demo.mq.consumer;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.Map;

public class MyTestConsumerInterceptor implements ConsumerInterceptor {
    @Override
    public ConsumerRecords onConsume(ConsumerRecords records) {
        //处理请求
        return records;
    }
    @Override
    public void close() {
        //关闭资源
    }
    @Override
    public void onCommit(Map offsets) {
        //提交消费位移后调用
    }
    @Override
    public void configure(Map<String, ?> configs) {
        //初始化
    }
}
