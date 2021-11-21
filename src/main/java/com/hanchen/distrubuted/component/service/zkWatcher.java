package com.hanchen.distrubuted.component.service;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class zkWatcher implements Watcher {
    static ZooKeeper zooKeeper;
    static {
        try {
            zooKeeper = new ZooKeeper("110.40.192.207:2181", 4000, new zkWatcher());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("eventType: " + watchedEvent.getType());
        if(watchedEvent.getType() == Event.EventType.NodeDeleted) {
            try {
                zooKeeper.exists(watchedEvent.getPath(), true);
            } catch (InterruptedException | KeeperException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, KeeperException, IOException {

        zooKeeper.create("/zookeeper/watch", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
        System.out.println("-----------------");
        Stat stat = zooKeeper.exists("/zookeeper/watch", true);
        System.in.read();
    }
}
