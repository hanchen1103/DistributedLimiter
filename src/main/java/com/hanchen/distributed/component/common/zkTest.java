package com.hanchen.distributed.component.common;

public class zkTest {

    public static class testThread implements Runnable {

        @Override
        public void run() {
            zkDistributedLock.setLockValue("aba");
            Boolean res = zkDistributedLock.getLock();
            System.out.println("-----------------------" + "Thread:" + res);
            zkDistributedLock.unLock();
        }
    }

    public static ZKDistributedLock zkDistributedLock;


    public static boolean test() throws InterruptedException {
        zkDistributedLock.setLockValue("aba");
        Boolean res = zkDistributedLock.getLock();
        //System.out.println("testing-----------");
        //System.out.println(res);
        //Thread.sleep(1000);
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
        zkDistributedLock = new ZKDistributedLock("/zookeeper", "110.40.192.207:2181", 4000);
        for(int i = 0; i < 10; i ++) {
            Thread t = new Thread(new testThread());
            t.start();
            Thread.sleep(100);
        }
//        Thread t = new Thread(new testThread());
//        t.start();
//        Thread a = new Thread(new testThread());
//        a.start();
    }
}
