package com.hanchen.distributed.component.common;

import com.hanchen.distributed.component.watchimpl.WatchDeleteEventImpl;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ZKDistributedLockImpl implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(ZKDistributedLockImpl.class);

    private ZooKeeper zooKeeper;

    /**
     * 加锁的对象
     */
    private String lockValue;

    /**
     * zookeeper锁路径
     */
    private String basePath = "/lock";

    private static final String split = "/";

    protected String connectionString;

    protected int sessionTimeout;

    private volatile Boolean isLock = null;

    private String currentLockName;

    private String preLockName;

    private volatile Boolean unLock = null;

    public void setIsLock() {
        this.isLock = true;
    }

    public boolean getIsLock() {
        while(isLock == null) {
            Thread.onSpinWait();
        }
        return this.isLock;
    }

    public String getCurrentLockName() {
        return this.currentLockName;
    }

    public ZKDistributedLockImpl lockValue(String lockValue) {
        this.lockValue = lockValue;
        return this;
    }

    public void setUnLock() {
        this.unLock = true;
    }

    public ZKDistributedLockImpl connectString(String connectionString) {
        this.connectionString = connectionString;
        return this;
    }

    public ZKDistributedLockImpl sessionTimeOut(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
        return this;
    }

    public ZKDistributedLockImpl basePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public ZKDistributedLockImpl create() {
        try {
            WatchDeleteEventImpl watchDeleteEvent = new WatchDeleteEventImpl();
            watchDeleteEvent.setZkDistributedLock(this);
            zooKeeper = new ZooKeeper(this.connectionString, this.sessionTimeout, watchDeleteEvent);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return this;
    }

    public void getLock() throws InterruptedException, KeeperException {
        currentLockName = zooKeeper.create(basePath + split + lockValue, "0".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        if(currentLockName == null) {
            throw new IllegalArgumentException("Lock failure");
        }
        List<String> list = zooKeeper.getChildren(basePath, false);
        list.sort(String::compareTo);
        String[] t = currentLockName.split("/");
        String lastName = t[t.length - 1];
        if (list.get(0).equals(lastName)) {
            isLock = true;
            logger.info("zookeeper get lock successful - lock is: " + currentLockName);
            return ;
        }
        preLockName = list.get(list.indexOf(lastName) - 1);
        zooKeeper.exists(basePath + split + preLockName, true);
    }

    public void unLock() throws InterruptedException, KeeperException {
        logger.info("-unlock: " + currentLockName + " successfully");
        zooKeeper.delete(currentLockName, -1);
        this.isLock = false;
        this.unLock = false;
        this.lockValue = null;
    }

    @Override
    public void run(){
        try {
            create();
            getLock();
            while(isLock == null || unLock == null || !isLock || !unLock) {
                Thread.onSpinWait();
            }
            unLock();
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }
}
