package ZkTest;

import com.hanchen.distributed.component.connectionpool.ZKConnectionEntity;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class zkTest {

    public static CountDownLatch countDownLatch = new CountDownLatch(1);

    static {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 4000,event -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        countDownLatch.await();

    }
}
