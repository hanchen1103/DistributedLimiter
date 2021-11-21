package com.hanchen.distrubuted.component.service;

public class zkTest {

    public static ZKDistributedLock zkDistributedLock;

    public static void main(String[] args) throws InterruptedException {
        zkDistributedLock = new ZKDistributedLock("/zookeeper", "110.40.192.207:2181", 4000);
        zkDistributedLock.setLockValue("aba");
        Boolean res = zkDistributedLock.getLock();
        System.out.println("testing-----------");
        System.out.println(res);
     //   Thread.sleep(1000 * 2);
//        zkDistributedLock.unLock();
    }
}
