package com.xyc.demo.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.Arrays;

public class CuratorTest {
    public static final String node = "/myTestNode6";
    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("47.104.92.51:2181")
                .sessionTimeoutMs(3000)  // 会话超时时间
                .connectionTimeoutMs(5000) // 连接超时时间
                .retryPolicy(retryPolicy)
                .build();

        client.start();
//        client.delete().deletingChildrenIfNeeded().forPath(node);

        //只监听一级子节点变更
        PathChildrenCache cache = new PathChildrenCache(client,node,true);
        cache.start(PathChildrenCache.StartMode.NORMAL);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                switch ( pathChildrenCacheEvent.getType()){
                    case CHILD_ADDED:
                    case CHILD_REMOVED:
                    case CHILD_UPDATED:
                    case INITIALIZED:
                    case CONNECTION_LOST:
                    case CONNECTION_SUSPENDED:
                    case CONNECTION_RECONNECTED:

                }
                System.out.println(pathChildrenCacheEvent.getType());
                System.out.println(pathChildrenCacheEvent.getData());
            }
        });


//        //被指定数据节点删除和数据更改，以及子节点数据更改
//        NodeCache cache1 = new NodeCache(client,node);
//        cache1.start(true);
//
//        cache1.getListenable().addListener(new NodeCacheListener() {
//            @Override
//            public void nodeChanged() throws Exception {
//                System.out.println("cash2"+cache1.getCurrentData());
//                if (cache1.getCurrentData()!=null){
//                    System.out.println(new String(cache1.getCurrentData().getData()));
//                }
//            }
//        });
        client.create().creatingParentsIfNeeded().forPath(node,"|".getBytes());

        client.create().creatingParentsIfNeeded().forPath(node+"/AA/aa");
        client.setData().forPath(node+"/AA/aa","a".getBytes());
        client.create().creatingParentsIfNeeded().forPath(node+"/AA/bb");
        client.setData().forPath(node+"/AA/bb","a".getBytes());
        client.setData().forPath(node,(node+"12").getBytes());
//        System.out.println(Arrays.toString(client.getData().forPath(node+"/AA/aa")));
        client.delete().guaranteed().deletingChildrenIfNeeded().forPath(node);
        Thread.sleep(5000);
//        client.create().creatingParentsIfNeeded().forPath(node,"|".getBytes());
//
//        client.create().creatingParentsIfNeeded().forPath(node+"/AA/aa");
//        client.delete().guaranteed().deletingChildrenIfNeeded().forPath(node);
//
//        Thread.sleep(5000);
    }
}
