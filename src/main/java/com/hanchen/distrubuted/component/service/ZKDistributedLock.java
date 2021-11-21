package com.hanchen.distrubuted.component.service;

public class ZKDistributedLock {

    ZKDistributedThread zkDistributedThread;

    public void setLockValue(String lockValue) {
        zkDistributedThread.setLockValue(lockValue);
    }

    public ZKDistributedLock(String basePath, String connectString, int sessinonTimeout) {
        zkDistributedThread = new ZKDistributedThread().create(basePath,
                connectString, sessinonTimeout);
    }

    public void unLock() {
        zkDistributedThread.isConnecting = false;
    }

    public Boolean getLock() {
        Thread thread = new Thread(zkDistributedThread);
        thread.start();
        while(zkDistributedThread.isLock == null) {
        }
        return zkDistributedThread.isLock;
    }

}
