package com.hanchen.distributed.component.common;

public class zkTest {

    public static ZKDistributedLock zkDistributedLock;

    public static boolean test() throws InterruptedException {
        zkDistributedLock = new ZKDistributedLock("/zookeeper", "110.40.192.207:2181", 4000);
        zkDistributedLock.setLockValue("aba");
        Boolean res = zkDistributedLock.getLock();
        System.out.println("testing-----------");
        System.out.println(res);
        zkDistributedLock.unLock();
        return res;
    }

    public static void main(String[] args) throws InterruptedException {
//        zkDistributedLock = new ZKDistributedLock("/zookeeper", "110.40.192.207:2181", 4000);
//        zkDistributedLock.setLockValue("aba");
//        Boolean res = zkDistributedLock.getLock();
//        System.out.println("testing-----------");
//        System.out.println(res);
        //zkDistributedLock.unLock();
        test();
        //Thread.sleep(1000 * 5);
        //test();

    }
}
