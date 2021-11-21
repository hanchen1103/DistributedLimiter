package com.hanchen.distrubuted.component.service;

import org.apache.zookeeper.*;

import java.io.IOException;

import java.util.concurrent.CountDownLatch;

public class ZkTest2 {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper("110.40.192.207:2181", 4000,
                watchedEvent -> {
                    if(watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        countDownLatch.countDown();
                    }
                });
        countDownLatch.await();
        System.out.println(zk.getState());

        String a = zk.create("/zookeeper/ababa", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);


//        zk.addWatch("zookeeper/locl/request", AddWatchMode.PERSISTENT_RECURSIVE);
        Thread.sleep(1000 * 30);
    }
}
