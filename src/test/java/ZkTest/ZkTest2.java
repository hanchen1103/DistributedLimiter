package ZkTest;

import org.apache.zookeeper.*;

import java.io.IOException;


public class ZkTest2 {


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        Long startTime = System.currentTimeMillis();
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 1000, event -> {
        });
        zooKeeper.create("/zookeeper/ababa", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.delete("/zookeeper/ababa", -1);

        Long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

//        zk.addWatch("zookeeper/locl/request", AddWatchMode.PERSISTENT_RECURSIVE);
    }
}
