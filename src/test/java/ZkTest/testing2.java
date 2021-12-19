package ZkTest;


import com.hanchen.distributed.component.common.ZKConnectionLock;
import org.apache.zookeeper.*;

import java.io.IOException;

public class testing2 {

    static ZooKeeper zk;

    public static void main(String[] args) throws InterruptedException, KeeperException, IOException {
        Long startTime = System.currentTimeMillis();
        zk = new ZooKeeper("127.0.0.1:2181", 4000, event -> {

        });
        zk.create("/zookeeper" + "/" + "1231231231", "0".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        Long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }


}