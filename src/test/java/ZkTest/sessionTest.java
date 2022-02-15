package ZkTest;

import com.hanchen.distributed.component.common.ZKDistributedLockImpl;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;


public class sessionTest {


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        Long startTime = System.currentTimeMillis();

        for (int i = 0; i < 10; i ++) {
            ZKDistributedLockImpl zkDistributedLock =
                    new ZKDistributedLockImpl().lockValue("chen").sessionTimeOut(4000).
                            connectString("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183");
            Thread thread = new Thread(zkDistributedLock);
            thread.start();
            zkDistributedLock.setUnLock();
        }
        Long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);


    }
}
