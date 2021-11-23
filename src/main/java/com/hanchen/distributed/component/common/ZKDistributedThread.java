package com.hanchen.distributed.component.common;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;


public class ZKDistributedThread implements Runnable {

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

    public volatile Boolean isLock = null;

    //private ZkLockWatcher zkLockWatcher;

    protected String connectionString;

    protected int sessionTimeout;

    public Long createTime;

    public void setLockValue(String lockValue) {
        this.lockValue = lockValue;
    }

//    @Override
//    public void process(WatchedEvent event) {
//        logger.info("eventType: " + event.getType());
//        if(event.getType() == Event.EventType.NodeDeleted) {
//            try {
//                zooKeeper.exists(event.getPath(), true);
//            } catch (KeeperException | InterruptedException e) {
//                e.printStackTrace();
//                logger.error(e.getMessage());
//            }
//        }
//    }

    public ZKDistributedThread create(String basePath, String connectString, int sessinonTimeout, String lockValue) {
        try {
            this.createTime = System.currentTimeMillis();
            this.connectionString = connectString;
            this.sessionTimeout = sessinonTimeout;
            this.lockValue = lockValue;
            //zkLockWatcher = new ZkLockWatcher();
            //zkLockWatcher.create(connectionString, sessionTimeout);
            zooKeeper = new ZooKeeper(connectString, sessinonTimeout, event -> {
            });
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
            isConnecting = isLock = false;
            logger.error(Thread.currentThread() + "get lock failure: " + isLock);
        }
        isLock = res != null;
        if(isLock) {
            logger.info(Thread.currentThread() + "zookeeper get lock successful - lock is: " + res);
        } else {
//            try {
//                zkLockWatcher.exists(basePath + "/" + lockValue);
//            } catch (KeeperException | InterruptedException e){
//                logger.error(e.getMessage());
//            }
            return ;
        }
        while (isConnecting) {
            Thread.onSpinWait();
        }
    }

    public void unLockNoBlocking() {
        try {
            if(isLock != null && isLock) {
                zooKeeper.delete(basePath + "/" + lockValue, -1);
                logger.info("unlock: " + basePath + "/" + lockValue + " successfully");
            }
        } catch (InterruptedException | KeeperException e) {
            logger.error(e.getMessage());
        } finally {
            isConnecting = false;
        }
    }


    @Override
    public void run() {
        getLockNoBlocking();
    }

}
