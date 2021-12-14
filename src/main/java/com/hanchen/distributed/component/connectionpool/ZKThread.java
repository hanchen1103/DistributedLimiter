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

public class ZKThread implements Runnable, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(ZKDistributedThread.class);

    @Serial
    private static final long serialVersionUID = -8215132982176492001L;

    /**
     * zk client
     */
    public static ZooKeeper zooKeeper;

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
    private static AtomicLong count = new AtomicLong(0);

    /**
     * zk lock base path
     */
    private static String basePath = "/zookeeper/lock";

    /**
     * lock name
     */
    private static String lockValue;

    /**
     * zk connection string
     */
    private static String connectionString;

    /**
     * zk connections timeout
     */
    private static Integer TimeOut = 4000;

    /**
     * Whether any threads use this lock currently
     */
    private static volatile AtomicBoolean inUse = new AtomicBoolean(false);

    public ZKThread connectionString(String connectionString) {
        ZKThread.connectionString = connectionString;
        return this;
    }

    public ZKThread lockValue(String lockValue) {
        ZKThread.lockValue = lockValue;
        return this;
    }

    public ZKThread basePath(String basePath) {
        ZKThread.basePath = basePath;
        return this;
    }

    public ZKThread timeOut(Integer timeOut) {
        ZKThread.TimeOut = timeOut;
        return this;
    }

    public void keepLockAndUnLock() throws InterruptedException, KeeperException, IllegalAccessException {
        while(inUse.get()) {
            logger.info("current session in use");
            Thread.onSpinWait();
        }
        inUse.set(true);
        if(lockValue == null) {
            throw new IllegalAccessException("lockValue param can't be empty");
        }
        while(!createFlag) {
            Thread.onSpinWait();
        }
        Object lockRes = null;
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
            keepLockAndUnLock();
        } catch (IOException | InterruptedException | KeeperException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }
}
