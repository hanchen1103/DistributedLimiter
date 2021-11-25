package com.hanchen.distributed.component.common;

import org.apache.zookeeper.*;

import java.io.IOException;

public class zkWatcher {


    public static void main(String[] args) throws InterruptedException, KeeperException, IOException {
       System.out.println(ZkdaemonThread.zooKeeper.getState());

    }
}
