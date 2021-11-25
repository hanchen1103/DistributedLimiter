package com.hanchen.distributed.component.common;

import org.apache.zookeeper.*;

import java.io.IOException;


public class ZkTest2 {

    static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        Long startTime = System.currentTimeMillis();
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 1000, event -> {
        });
        zooKeeper.create("/zookeeper/ababa", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.delete("/zookeeper/ababa", -1);

        Long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

//        zk.addWatch("zookeeper/locl/request", AddWatchMode.PERSISTENT_RECURSIVE);
    }
}
