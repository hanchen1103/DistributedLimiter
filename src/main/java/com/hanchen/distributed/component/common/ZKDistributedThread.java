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

    protected String connectionString;

    protected int sessionTimeout;

    public Long createTime;

    public volatile Boolean timeToUnLock = false;


    public ZKDistributedThread create(String basePath, String connectString, int sessinonTimeout, String lockValue) {
        try {
            this.createTime = System.currentTimeMillis();
            this.connectionString = connectString;
            this.sessionTimeout = sessinonTimeout;
            this.lockValue = lockValue;
            zooKeeper = new ZooKeeper(connectString, sessinonTimeout, event -> {
            });
            this.basePath = basePath;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return this;
    }

    public void getLockNoBlocking() {
        String res;
        try {
            res = zooKeeper.create(basePath + "/" + lockValue, "0".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException | InterruptedException e) {
            isConnecting = isLock = false;
            logger.error(Thread.currentThread().getName() + "-zookeeper get lock failure: " + isLock);
            return ;
        }
        isLock = res != null;
        if(isLock) {
            logger.info(Thread.currentThread().getName() + "-zookeeper get lock successful - lock is: " + res);
            try {
                while(!timeToUnLock) {
                    Thread.onSpinWait();
                }
                zooKeeper.delete(basePath + "/" + lockValue, -1);
                logger.info(Thread.currentThread().getName() + "-unlock: " + basePath + "/" + lockValue + " successfully");
            } catch (InterruptedException | KeeperException e) {
                logger.error(e.getMessage());
            } finally {
                isConnecting = false;
            }
        }

    }

    @Override
    public void run() {
        getLockNoBlocking();
    }

}
