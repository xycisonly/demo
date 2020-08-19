package com.xyc.demo.mq.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class mian {
    public static void main(String[] args)  {
        //必选配置参数
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.0.1,8080,192.168.0.1,8080,192.168.0.1,8080");//broker，逗号分隔
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());//key序列化
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());//value序列化
        //非必选参数
        properties.put(ProducerConfig.CLIENT_ID_CONFIG,"demo1");//client标识
        properties.put(ProducerConfig.RETRIES_CONFIG,3);//发送失败，重试次数
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,MyTestPartitioner.class.getName());//设置分区计算器
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,MyTestProducerInterceptor.class.getName()+","+MyTestProducerInterceptor.class.getName());//配置过滤器，可配置多个

        //kafka生产者
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);
        ProducerRecord<String, String> record = new ProducerRecord<>("topicName", "value");
        //发送消息
        //1  异步发送不关心结果
        producer.send(record);
        //2  同步发送，RecordMetadata含有partition和offset等信息，发送失败抛出异常
        try {
            RecordMetadata recordMetadata = producer.send(record).get();
            System.out.println(recordMetadata.partition()+"+"+recordMetadata.offset());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        //3  异步发送，异步处理结果，RecordMetadata和Exception一定有一个为空
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception!=null){
                    System.out.println(exception);
                }else {
                    System.out.println("发送成功"+metadata.topic());
                }

            }
        });
    }
}
