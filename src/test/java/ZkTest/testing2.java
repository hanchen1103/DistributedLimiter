package ZkTest;

import org.apache.zookeeper.*;

import java.io.IOException;

public class testing2 {

    static ZooKeeper zk;

    public static void main(String[] args) throws InterruptedException, KeeperException, IOException {
        Long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i ++) {
            zk = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", 4000, event -> {
            });
        }

//        zk.create("/zookeeper" + "/" + "lock/test", "zhc".getBytes(),
//                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
//        zk.create("/zookeeper" + "/" + "lock/test", "zhc".getBytes(),
//                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        Long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        Thread.sleep(100000);
    }

}