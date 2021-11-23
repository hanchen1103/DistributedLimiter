package com.hanchen.distributed.component.common;

import org.apache.zookeeper.*;

import java.io.IOException;


public class ZkTest2 {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zk = new ZooKeeper("110.40.192.207:2181", 1000, event -> {
        });

        Long startTime = System.currentTimeMillis();
        String a = zk.create("/zookeeper/ababa", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zk.delete("/zookeeper/ababa", -1);
        Long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        System.in.read();
//        zk.addWatch("zookeeper/locl/request", AddWatchMode.PERSISTENT_RECURSIVE);
    }
}
