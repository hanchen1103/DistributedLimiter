package com.hanchen.distributed.component.common;

import com.hanchen.distributed.component.connectionpool.ZKConnectionEntity;

public class ZKConnectionLock {


    public void lock(String lockValue) {
        ZKConnectionEntity zkConnectionEntity = new ZKConnectionEntity().lockValue(lockValue);

        zkConnectionEntity.setFlagToLockOrUnLock(1);

        while(!zkConnectionEntity.createFlag) {
            Thread.onSpinWait();
        }

    }

    public void unlock(String lockValue) {
        ZKConnectionEntity zkConnectionEntity = new ZKConnectionEntity().lockValue(lockValue);
        zkConnectionEntity.setFlagToLockOrUnLock(0);
        //zkConnectionPool.addConnection(zkConnectionEntity.initConnection());
    }
}
