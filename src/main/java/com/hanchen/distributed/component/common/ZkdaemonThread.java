package com.hanchen.distributed.component.common;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZkdaemonThread{

    public static ZooKeeper zooKeeper;

    static {
        try {
            zooKeeper = new ZooKeeper("127.0.0.1:2181", 1000, event -> {
            });
            while(true) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
