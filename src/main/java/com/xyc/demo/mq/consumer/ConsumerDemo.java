package com.xyc.demo.mq.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

public class ConsumerDemo {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//key反序列化方式
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//value看序列化方式
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "122.9.110.190:9094,122.9.98.194:9094,122.9.108.192:9094");//broker地址
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId1");//组名
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
//        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "10000");
//        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RangeAssignor.class.getName());
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 2);//拉两个
        KafkaConsumer<String, String> consumer1 = new KafkaConsumer<>(properties);
        TopicPartition topicName1 = new TopicPartition("topicName1", 1);
//        consumer1.assign(Collections.singletonList(topicName1));//订阅指定主题的指定分区
        consumer1.subscribe(Arrays.asList("topicName1"));
        Map<TopicPartition, OffsetAndTimestamp> topicPartitionOffsetAndTimestampMap = consumer1.offsetsForTimes();

//        Properties properties2 = new Properties();
//        properties2.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//key反序列化方式
//        properties2.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//value看序列化方式
//        properties2.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "122.9.110.190:9094,122.9.98.194:9094,122.9.108.192:9094");//broker地址
//        properties2.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId1");//组名
//        properties2.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 2);//拉两个
//        properties2.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
//        KafkaConsumer<String, String> consumer2 = new KafkaConsumer<>(properties2);
//        consumer2.assign(Collections.singletonList(topicName1));//订阅指定主题的指定分区

//        consumer2.seek(new TopicPartition("topicName1",1),349);
//        while (true){
//            HashMap<TopicPartition,OffsetAndMetadata> map = new HashMap<>();
//            OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(349);
//            map.put(topicName1,offsetAndMetadata);
//            consumer2.commitSync(map);
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                while (true){
                    consumer1.commitSync();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

        while (true){
            System.out.println("=====");
            ConsumerRecords<String, String> records = consumer1.poll(Duration.ofMillis(1000L));
            for (ConsumerRecord<String, String> record:records){
                System.out.println("client1"+record.value());
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            consumer1.commitSync();
        }
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

    }
}
