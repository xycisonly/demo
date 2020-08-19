package com.xyc.demo.mq.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        //基础配置

        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//key反序列化方式
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//value看序列化方式
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.1:8080,192.168.0.1:8080");//broker地址
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");//组名
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "clientId");//客户端id，一般不写自动生成

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        //获取partition信息
        List<PartitionInfo> partitionInfos = consumer.partitionsFor("topicName");//获取topic相关信息
        PartitionInfo partitionInfo = partitionInfos.get(0);
        System.out.println(partitionInfo.partition() + partitionInfo.leader().id() + partitionInfo.topic());

        //订阅与取消
        List<String> topicList = Arrays.asList("topic1", "topic2");
        consumer.subscribe(topicList);//订阅topic，自动调整订阅的分区
        consumer.subscribe(Pattern.compile("topicName*"));//订阅topic，正则表达式
        consumer.assign(Collections.singletonList(new TopicPartition("topic", 1)));//订阅指定主题的指定分区

        consumer.unsubscribe();//取消订阅

        //消费前强制指定消费位置
        Set<TopicPartition> ass = new HashSet<>();
        while (ass.size() == 0) {
            consumer.poll(Duration.ofSeconds(1L));
            //获取当前被分配的partition
            ass = consumer.assignment();
        }
        //移动到尾部（复杂版）
        Map<TopicPartition, Long> topicPartitionLongMap = consumer.endOffsets(ass);
//        Map<TopicPartition, Long> topicPartitionLongMap = consumer.beginningOffsets(ass);  //头部位移
        for (TopicPartition partition : ass) {
            //为分区设定消费的offset
            consumer.seek(partition, topicPartitionLongMap.get(partition));
        }
        //移动到尾部
        consumer.seekToBeginning(ass);
        //移动到头部
        consumer.seekToEnd(ass);
        //移动大于某时间的消息,省略使用seek方法
        HashMap<TopicPartition, Long> partitionLongHashMap = new HashMap<>();
        Map<TopicPartition, OffsetAndTimestamp> partitionOffsets = consumer.offsetsForTimes(partitionLongHashMap);


        //获取信息
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        try {
            while (atomicBoolean.get()) {//拉取消费
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000L));
                //第一种正常消费
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.key().toString() + record.topic() + record.partition() + record.offset() + record.value());
                    //每读取一条消息进行提交
                    consumer.commitSync(Collections.singletonMap(
                            new TopicPartition(record.topic(), record.partition()),
                            new OffsetAndMetadata(record.offset() + 1)));
                }
                //第二种 分partition消费
                for (TopicPartition topicPartition : records.partitions()) {
                    List<ConsumerRecord<String, String>> records1 = records.records(topicPartition);
                    for (ConsumerRecord<String, String> record : records1) {
                        System.out.println(record);
                    }
                    //分partition进行提交
                    consumer.commitSync(Collections.singletonMap(topicPartition,
                            new OffsetAndMetadata(records1.get(records1.size() - 1).offset() + 1)));
                }
                //第三种根据订阅的不同topic处理
                for (String topic : topicList) {
                    for (ConsumerRecord<String, String> record1 : records.records(topic)) {
                        System.out.println(record1);
                    }
                    //建议自动提交
                }
                consumer.commitSync();//同步提交偏移量，提交的是下次拉取的位置，默认不用写。
                //异步提交
                consumer.commitAsync((map, e) -> {
                    if (e != null) System.out.println(e);
                });

                //暂停某些topic的partition拉取信息
                int partition = 0;
                consumer.pause(Collections.singleton(new TopicPartition("topicName", partition)));
                //恢复消费
                consumer.resume(Collections.singleton(new TopicPartition("topicName", partition)));
                //获取暂停partition
                Set<TopicPartition> paused = consumer.paused();

                //退出poll循环，可以让别的线程调用的唯一安全方法，poll方法会抛出WakeupException
                consumer.wakeup();
            }
        } catch (WakeupException wke) {
            //do nothing
        } catch (Exception e) {
            //log
        } finally {
            //关闭
            consumer.close();
            //关闭等待的时间
            consumer.close(Duration.ofMillis(1000L));
        }


    }
}
