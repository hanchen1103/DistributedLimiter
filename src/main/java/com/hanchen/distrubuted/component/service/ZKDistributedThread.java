package com.hanchen.distrubuted.component.service;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;


public class ZKDistributedThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(ZKDistributedThread.class);


    private ZooKeeper zooKeeper;

    /**
     * 加锁的对象
     */
    private String lockValue;

    /**
     * zookeeper锁路径
     */
    private String basePath = "/lock";


    public volatile boolean isConnecting = true;

    public volatile boolean isLock = false;

    public void setLockValue(String lockValue) {
        this.lockValue = lockValue;
    }


    public ZKDistributedThread create(String basePath, String connectString, int sessinonTimeout) {
        try {
            zooKeeper = new ZooKeeper(connectString, sessinonTimeout, event -> {
            });
            logger.info(String.valueOf(zooKeeper.getState()));
            this.basePath = basePath;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return this;
    }

    public void getLockNoBlocking() {
        String res = null;
        try {
            res = zooKeeper.create(basePath + "/" + lockValue, "0".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException | InterruptedException e) {
            logger.error("occconnecting error or zookeeper path error");
        }
        isLock = res != null;
        if(isLock) {
            logger.info("zookeeper get lock successful - lock is: " + res);
        }
        try {
            while(isConnecting) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException exception) {
            logger.error(exception.getMessage());
        }
    }


    @Override
    public void run() {
        getLockNoBlocking();
    }
}
