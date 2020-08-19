package com.xyc.demo.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class zktest {
    private static CountDownLatch latch = new CountDownLatch(1);
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException, NoSuchAlgorithmException {
        String createNodeName= "/myTestNode";
        String createNodeChildName= "/myTestNode/child-";
        ZooKeeper zk = new ZooKeeper("47.95.4.97:2181", 3000, new TestWatch());
        latch.await();
//        ZooKeeper zk2 = new ZooKeeper("47.95.4.97:2181", 3000, new TestWatch());
//        latch.await();
//        zk.exists()
//        zk.getData(createNodeName,true,null);
        List<String> children = zk.getChildren("/kafka/brokers/ids", false, null);
        System.out.println("aa"+children);
//        zk.create(createNodeName,createNodeName.getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
//        zk2.delete(createNodeName,-1);

        try {
            Thread.sleep(5000);
        }catch (Exception e){}
//        zk.create(createNodeChildName,createNodeChildName.getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
//        zk.create(createNodeChildName,createNodeChildName.getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
//        System.out.println(zk.getChildren(createNodeName,false));
//        zk.delete("/myTestNode1/child-0000000001",-1);
//        System.out.println(zk.getChildren(createNodeName,false));
//        zk.create(createNodeChildName,createNodeChildName.getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
//        System.out.println(zk.getChildren(createNodeName,false));
    }
    static class TestWatch implements Watcher {
        @Override
        public void process(WatchedEvent watchedEvent) {
            switch (watchedEvent.getState()) {

                case SyncConnected:
                    Event.EventType type = watchedEvent.getType();
                    switch (type){
                        case None:
                            System.out.println("链接成功1");
                            latch.countDown();
                            break;
                        case NodeCreated:
                            break;
                        case NodeDeleted:
                            break;
                        case NodeDataChanged:
                            break;
                        case NodeChildrenChanged:
                            break;
                    }
                    System.out.println("链接正常:"+type);
                    break;
                case Expired:
                    System.out.println("已过期");
                    break;
                case AuthFailed:
                    System.out.println("验证失败");
                    break;
                case Disconnected:
                    System.out.println("断线");
                    break;
                case ConnectedReadOnly:
                    System.out.println("只读链接");
                    break;
                case SaslAuthenticated:
                    System.out.println("sasl认证");
                    break;
                default:
                    System.out.println(watchedEvent.getState()+"事件");
            }
        }
    }
}
