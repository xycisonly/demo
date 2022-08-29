package com.xyc.demo.mq.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Demo {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"122.9.110.190:9094,122.9.98.194:9094,122.9.108.192:9094");//broker，逗号分隔
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());//key序列化
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());//value序列化
        //非必选参数
//        properties.put(ProducerConfig.CLIENT_ID_CONFIG,"demo1");//client标识
//        properties.put(ProducerConfig.RETRIES_CONFIG,3);//发送失败，重试次数
//        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,MyTestPartitioner.class.getName());//设置分区计算器
//        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,MyTestProducerInterceptor.class.getName()+","+MyTestProducerInterceptor.class.getName());//配置过滤器，可配置多个

        //kafka生产者
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);
        for (int index = 0;index<100;index++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("topicName1",1,"key5", "5value"+index);
            producer.send(record);
        }


        try {
            Thread.sleep(10000L);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        producer.close();
    }
}
