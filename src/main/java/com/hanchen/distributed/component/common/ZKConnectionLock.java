package com.hanchen.distributed.component.common;

import com.hanchen.distributed.component.connectionpool.ZKConnectionEntity;
import com.hanchen.distributed.component.connectionpool.ZKConnectionPool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZKConnectionLock {

    private static ZKConnectionPool zkConnectionPool = ZKConnectionPool.getInstance();


    public void lock(String lockValue) {
        Long startTime = System.currentTimeMillis();
        ZKConnectionEntity zkConnectionEntity = zkConnectionPool.getConnection().lockValue(lockValue);
        zkConnectionEntity.setFlagToLockOrUnLock(1);
        while(!zkConnectionEntity.createFlag) {
            Thread.onSpinWait();
        }
        Long endTime = System.currentTimeMillis();
        zkConnectionPool.addConnection(zkConnectionEntity.initConnection());
        System.out.println(endTime - startTime);
    }

    public void unlock(String lockValue) {
        ZKConnectionEntity zkConnectionEntity = zkConnectionPool.getConnection().lockValue(lockValue);
        zkConnectionEntity.setFlagToLockOrUnLock(0);
        //zkConnectionPool.addConnection(zkConnectionEntity.initConnection());
    }
}
