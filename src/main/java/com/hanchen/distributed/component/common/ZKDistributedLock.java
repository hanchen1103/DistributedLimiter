//package com.hanchen.distributed.component.common;
//
//import java.util.Comparator;
//import java.util.concurrent.ConcurrentSkipListSet;
//
//public class ZKDistributedLock {
//
//
//    private String connectString;
//
//    private String basePath;
//
//    private int sessionTimeout;
//
//    private String lockValue;
//
//
//    /**
//     * 设置加锁值
//     * @param lockValue 锁值
//     */
//    public void setLockValue(String lockValue) {
//        this.lockValue = lockValue;
//    }
//
//    public ZKDistributedLock(String basePath, String connectString, int sessionTimeout) {
//        this.basePath = basePath;
//        this.connectString = connectString;
//        this.sessionTimeout = sessionTimeout;
//    }
//
//    /**
//     * 手动解锁
//     */
//    public void unLock() {
//
//    }
//
//    /**
//     * 获取锁
//     * @return 是否获取到锁
//     */
//    public Boolean getLock() {
//        ZKDistributedLockImpl zkDistributedThread = new ZKDistributedLockImpl().
//                create(this.basePath, this.connectString, this.sessionTimeout, this.lockValue);
//        Thread thread = new Thread(zkDistributedThread);
//        thread.start();
//        while(zkDistributedThread.isLock == null) {
//            Thread.onSpinWait();
//        }
//        if(zkDistributedThread.isLock) {
//            zkDistributedThreadSet.add(zkDistributedThread);
//        }
//        return zkDistributedThread.isLock;
//    }
//
//}
