package ZkTest;


import com.hanchen.distributed.component.watchimpl.WatchDeleteEventImpl;
import org.apache.zookeeper.*;

import java.io.IOException;

public class zkTest {

    static ZooKeeper zk;

    public static void main(String[] args) throws InterruptedException, KeeperException, IOException {

        zk = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", 4000, new WatchDeleteEventImpl());
        Long startTime = System.currentTimeMillis();
        Long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        Thread.sleep(1000000);
    }
}
