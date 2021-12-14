package com.hanchen.distributed.component.connectionpool;

import com.hanchen.distributed.component.common.ZKDistributedThread;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class ZKThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(ZKDistributedThread.class);

    public static ZooKeeper zooKeeper;

    public static volatile boolean createFlag = false;

    public static volatile boolean deleteFlag = false;

    private static AtomicLong count = new AtomicLong(0);

    private static String basePath = "/zookeeper/lock";

    private static String lockValue;

    private static String connectionString;

    private static Integer TimeOut = 4000;

    public void keepLockAndUnLock() throws InterruptedException, KeeperException, IllegalAccessException {
        if(lockValue == null) {
            throw new IllegalAccessException("lockValue param can't be empty");
        }
        while(!createFlag) {
            Thread.onSpinWait();
        }
        Object lockRes = zooKeeper.create(basePath + "/" + lockValue, "0".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        logger.info(Thread.currentThread().getName() + "-zookeeper get lock successful - lock is: " + lockRes);
        createFlag = false;
        while(!deleteFlag) {
            Thread.onSpinWait();
        }
        zooKeeper.delete("/zookeeper/lock/abab", -1);
        deleteFlag = false;
        logger.info(Thread.currentThread().getName() + "-unlock: " + basePath + "/" + lockValue + " successfully");
        count.addAndGet(1);
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
