package ZkTest;

import com.hanchen.distributed.component.common.RSDistributedLock;
import redis.clients.jedis.Jedis;

public class RsLockTest {

    public static RSDistributedLock rsDistributedLock;

    static {
        Jedis jedis = new Jedis("redis://127.0.0.1:6379/0");
        rsDistributedLock =
                new RSDistributedLock.RsLockBuilder<>(jedis).lockTime(5000L).requestKey("lock").requestValue("abc").create();
    }

    public static class myThread implements Runnable {

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            rsDistributedLock.getLock();
            rsDistributedLock.unLock();
            long endTime = System.currentTimeMillis();
            System.out.println(endTime - startTime);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < 5; i ++) {
            Thread t = new Thread(new myThread());
            t.start();
        }
    }
}
