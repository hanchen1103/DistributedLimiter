package ZkTest;

import com.hanchen.distributed.component.common.ZKDistributedThread;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.*;

public class sessionTest {

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        List<Thread> list = new ArrayList<>();
        String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        int sessionTimeout = 4000;
        ZKDistributedThread zkDistributedThread = new ZKDistributedThread().create("/zookeeper/lock",
                    connectString, sessionTimeout, "aba");
        Thread a = new Thread(zkDistributedThread);
        a.start();
//        for(int i = 0; i < 3; i ++) {
//            ZKDistributedThread zkDistributedThread = new ZKDistributedThread().create("/zookeeper/lock",
//                    connectString, sessionTimeout, "aba");
//            Thread t = new Thread(zkDistributedThread);
//            list.add(t);
//        }
//        Thread a = list.get(0);
//        a.start();
        Thread.sleep(2000);
    }
}
