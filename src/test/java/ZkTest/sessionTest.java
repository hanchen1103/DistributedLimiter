package ZkTest;


import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.*;

public class sessionTest {

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        List<Thread> list = new ArrayList<>();
        String connectString = "127.0.0.1:2181";
        int sessionTimeout = 4000;
        ZooKeeper zooKeeper = new ZooKeeper(connectString, sessionTimeout, event -> {
        });
        Object res = "fail";
        try {
            res = zooKeeper.create("/zookeeper/locqk/qw23e", "0".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException  e) {
            e.getMessage();
        }

        System.out.println("lock:" + res);
        Thread.sleep(5000);
    }
}
