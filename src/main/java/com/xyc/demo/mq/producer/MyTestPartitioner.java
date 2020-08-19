package com.xyc.demo.mq.producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class MyTestPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        //计算分区号

        //默认方法：若keyBytes为空轮询partition发送，不为空根据keyBytes使用hash算法计算得到
        return 0;
    }

    @Override
    public void close() {
        //关闭回收资源
    }

    @Override
    public void configure(Map<String, ?> configs) {
    //初始化配置
    }
}
