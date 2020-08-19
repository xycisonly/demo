package com.xyc.demo.mq.producer;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class MyTestProducerInterceptor implements ProducerInterceptor {
    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        //可以对record进行更改，先于partitioner和serializer
        return null;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        //出现异常或者正常返回时，会调用这个方法metadata和exception一定有一个为null
        //当用户线程出现问题，也会调用这个方法，若过于复杂会影响效率。
        //当sender线程出现，也会调用此方法，在Callback被调用之前调用
    }

    @Override
    public void close() {
        //关闭资源
    }

    @Override
    public void configure(Map<String, ?> configs) {
        //初始化
    }
}
