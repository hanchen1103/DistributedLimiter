package ZkTest;

import com.hanchen.distributed.component.common.ZKDistributedThread;
import com.hanchen.distributed.component.connectionpool.ZKThread;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.*;

public class sessionTest {

    public static ZKThread zkThread;

    public static class testThreaduse implements Runnable {

        @Override
        public void run() {
            zkThread.createFlag = true;
//            zkThread.deleteFlag = true;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zkThread = new ZKThread().connectionString("127.0.0.1:2181").
                timeOut(4000).basePath("/zookeeper/lock").lockValue("cdcdcd");
        Thread thread = new Thread(zkThread);
        thread.start();
        for(int i = 0; i < 10; i ++) {
            Thread th = new Thread(new testThreaduse());
            th.start();
            Thread.sleep(1000);
        }

    }
}
