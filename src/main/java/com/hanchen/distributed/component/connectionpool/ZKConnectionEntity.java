package com.hanchen.distributed.component.connectionpool;

import com.hanchen.distributed.component.common.ZKDistributedThread;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ZKConnectionEntity implements Runnable, Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ZKDistributedThread.class);

    @Serial
    private static final long serialVersionUID = -8215132982176492001L;

    /**
     * zk client
     */
    private ZooKeeper zooKeeper;

    /**
     * Whether to create a node (lock)
     */
    public volatile Boolean createFlag = false;

    /**
     * Whether to delete a node (unlock)
     */
    public volatile Boolean deleteFlag = false;

    /**
     * Session frequency
     */
    private AtomicLong count = new AtomicLong(0);

    /**
     * zk lock base path
     */
    private String basePath = "/zookeeper/lock";

    /**
     * lock name
     */
    private volatile String lockValue;

    /**
     * zk connection string
     */
    private String connectionString;

    /**
     * zk connections timeout
     */
    private Integer TimeOut = 4000;

    /**
     * Whether any threads use this lock currently
     */
    private volatile AtomicBoolean inUse = new AtomicBoolean(false);

    /**
     * flag 1: lock 0: unlock -1: wait
     */
    public volatile Integer flagToLockOrUnLock = -1;

    private static final Object obj = new Object();


    public ZKConnectionEntity initConnection() {
        this.flagToLockOrUnLock = -1;
        this.lockValue = null;
        this.createFlag = false;
        this.deleteFlag = false;
        return this;
    }

    public void setFlagToLockOrUnLock(int isLock) {
        synchronized (obj) {
            if(inUse.get()) {
                logger.info("current session is occupied");
                return ;
            }
            inUse.set(true);
            this.flagToLockOrUnLock = isLock;
            obj.notify();
        }
    }


    public void lock() {

        Object lockRes = null;
        try {

            lockRes = zooKeeper.create(basePath + "/" + lockValue, "0".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.info(Thread.currentThread().getName() + "-zookeeper get lock successful - lock is: " + lockRes);
            createFlag = true;

        } catch (KeeperException | InterruptedException e) {
            logger.info(e.getMessage());
        } finally {
            inUse.set(false);
        }
        if(lockRes == null) {
            logger.info("getLock failure");
        }
    }


    public void unlock() {
        try {
            zooKeeper.delete(basePath + "/" + lockValue, -1);
            deleteFlag = true;
            logger.info(Thread.currentThread().getName() + "-zookeeper freed lock successful - unlock is " + basePath + "/" + lockValue);
        } catch (InterruptedException | KeeperException e) {
            logger.info("freedLock failure");
        } finally {
            inUse.set(false);
        }
    }

    public void keepConnectionWaitLockAndUnLock() throws InterruptedException {
        synchronized (obj) {
            while(flagToLockOrUnLock == -1) {
                obj.wait();
            }
        }
        if(flagToLockOrUnLock == 1) {
            lock();
        } else {
            unlock();
        }
        flagToLockOrUnLock = -1;
        keepConnectionWaitLockAndUnLock();
    }


    public ZKConnectionEntity connectionString(String connectionString) {
        this.connectionString = connectionString;
        return this;
    }

    public ZKConnectionEntity lockValue(String lockValue) {
        this.lockValue = lockValue;
        return this;
    }

    public ZKConnectionEntity basePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public ZKConnectionEntity timeOut(Integer timeOut) {
        this.TimeOut = timeOut;
        return this;
    }


    public void keepLockAndUnLock() throws InterruptedException, KeeperException, IllegalAccessException {
        while(lockValue == null) {
            Thread.onSpinWait();
        }
        Object lockRes = null;
        while(!createFlag) {
            Thread.onSpinWait();
        }
        try {
            lockRes = zooKeeper.create(basePath + "/" + lockValue, "0".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.info(Thread.currentThread().getName() + "-zookeeper get lock successful - lock is: " + lockRes);
            createFlag = false;
        } catch (KeeperException | InterruptedException e) {
            logger.info(e.getMessage());
        }
        if(lockRes == null) {
            logger.info("getLock failure");
            inUse.set(false);
            keepLockAndUnLock();
        }
        while(!deleteFlag) {
            Thread.onSpinWait();
        }
        zooKeeper.delete(basePath + "/" + lockValue, -1);
        deleteFlag = false;
        logger.info(Thread.currentThread().getName() + "-zookeeper freed lock successful - unlock is " + lockRes);
        count.addAndGet(1);
        inUse.set(false);
        keepLockAndUnLock();
    }

    @Override
    public void run() {
        if(connectionString == null ) {
            try {
                throw new IllegalAccessException("connectionString empty exception");
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage());
            }
        }
        try {
            zooKeeper = new ZooKeeper(connectionString, TimeOut, event -> {
            });
            keepConnectionWaitLockAndUnLock();
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

}
