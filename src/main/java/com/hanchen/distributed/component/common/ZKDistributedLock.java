package com.hanchen.distributed.component.common;


public class ZKDistributedLock {


    private String connectionString;

    private String basePath;

    private int sessionTimeout;

    private String lockValue;

    public ZKDistributedLock connectString(String connectionString) {
        this.connectionString = connectionString;
        return this;
    }

    public ZKDistributedLock sessionTimeOut(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
        return this;
    }

    public ZKDistributedLock basePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public ZKDistributedLock lockValue(String lockValue) {
        this.lockValue = lockValue;
        return this;
    }

    ZKDistributedLockImpl zkDistributedThread;

    public ZKDistributedLock ZKLockBuilder() {
         zkDistributedThread = new ZKDistributedLockImpl().lockValue(lockValue).sessionTimeOut(sessionTimeout).
                connectString(this.connectionString);
        return this;
    }

    /**
     * 手动解锁
     */
    public void unLock() {
        zkDistributedThread.setUnLock();
    }

    /**
     * 获取锁
     * @return 是否获取到锁
     */
    public Boolean getLock() {
        Thread thread = new Thread(zkDistributedThread);
        thread.start();
        return zkDistributedThread.getIsLock();
    }

}
