package ZkTest;

import com.hanchen.distributed.component.connectionpool.ZKConnectionEntity;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


public class sessionTest {



    public static void testThreaduse() {

        ZKConnectionEntity zkThread = new ZKConnectionEntity().connectionString("127.0.0.1:2181").
                timeOut(4000).basePath("/zookeeper/lock").lockValue(String.valueOf("aba"));
        zkThread.createFlag = true;
        zkThread.deleteFlag = true;

    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        Long startTime = System.currentTimeMillis();
        ZKConnectionEntity zkThread1 = new ZKConnectionEntity().connectionString("127.0.0.1:2181").
                timeOut(4000000).basePath("/zookeeper/lock").lockValue(String.valueOf(2));
        Thread thread1 = new Thread(zkThread1);
        thread1.start();
        zkThread1.createFlag = true;
        zkThread1.deleteFlag = true;
        Thread.sleep(2000);
        zkThread1.createFlag = true;
        Long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}
