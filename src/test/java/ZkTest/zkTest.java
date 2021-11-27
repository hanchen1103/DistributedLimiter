package ZkTest;

import com.hanchen.distributed.component.common.ZKDistributedLock;

public class zkTest {

    public static class testThread implements Runnable {

        @Override
        public void run() {
            zkDistributedLock.setLockValue("aba");
            Boolean res = zkDistributedLock.getLock();
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

        zkDistributedLock =
                new ZKDistributedLock("/zookeeper", "127.0.0.1:2181", 1000);
        Long start = System.currentTimeMillis();
        for(int i = 0; i < 10; i ++) {
            Thread t = new Thread(new testThread());
            t.start();
            Thread.sleep(100);
        }
        Long end = System.currentTimeMillis();

//        for(int i = 0; i < 10; i ++)
//        {
//            zkDistributedLock.setLockValue("ababab");
//            zkDistributedLock.getLock();
//            zkDistributedLock.unLock();
//        }
        System.out.println(end - start);
//        Thread a = new Thread(new testThread());
//        a.start();
    }
}