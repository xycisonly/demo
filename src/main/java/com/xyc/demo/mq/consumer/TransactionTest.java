package com.xyc.demo.mq.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * @author xiaoyuchen
 * @date 2022/5/6
 */
public class TransactionTest {

//    static {
//        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
//        Logger root = loggerContext.getLogger("root");
//        root.(Level.INFO);
//    }
    public static void main(String[] args) throws InterruptedException {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//key反序列化方式
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//value看序列化方式
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.228.202.96:9092");//broker地址
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "com.maoyan.transaction.test.group");//组名
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.assign(Collections.singletonList(new TopicPartition("com.maoyan.transaction.test", 0)));
        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000L));
            for (ConsumerRecord<String, String> record : consumerRecords) {
                System.out.println("123321"+record.value().toString());
            }
            Thread.sleep(5000);
        }

    }
}
