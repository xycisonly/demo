package com.xyc.demo.mq.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author xiaoyuchen
 * @date 2022/5/6
 */
public class ProducerTransactionTest {
    public static void main(String[] args) throws InterruptedException {
        Properties properties1 = new Properties();
        properties1.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"10.228.202.96:9092");//broker，逗号分隔
        properties1.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());//key序列化
        properties1.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());//value序列化
        properties1.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,"true");//broker，逗号分隔
        properties1.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,"TRANSACTIONAL_ID_CONFIG1");//broker，逗号分隔
        KafkaProducer<String,String> producer1 = new KafkaProducer<>(properties1);
        producer1.initTransactions();
        producer1.beginTransaction();
        ProducerRecord<String, String> record1 = new ProducerRecord<>("com.maoyan.transaction.test", "value10");
        producer1.send(record1);

        Properties properties2 = new Properties();
        properties2.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"10.228.202.96:9092");//broker，逗号分隔
        properties2.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());//key序列化
        properties2.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());//value序列化
        properties2.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,"true");//broker，逗号分隔
        properties2.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,"TRANSACTIONAL_ID_CONFIG2");//broker，逗号分隔
        KafkaProducer<String,String> producer2 = new KafkaProducer<>(properties2);
        producer2.initTransactions();
        producer2.beginTransaction();
        ProducerRecord<String, String> record2 = new ProducerRecord<>("com.maoyan.transaction.test", "value20");
        producer2.send(record2);


        producer2.commitTransaction();
        Thread.sleep(12000);
        producer1.commitTransaction();
        producer2.close();
        producer1.close();
    }
}
