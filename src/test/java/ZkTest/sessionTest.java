package ZkTest;

import com.hanchen.distributed.component.common.ZKConnectionLock;
import com.hanchen.distributed.component.connectionpool.ZKConnectionEntity;
import com.hanchen.distributed.component.connectionpool.ZKConnectionPool;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class sessionTest {


    static final Object object = new Object();


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        ZKConnectionLock zkConnectionLock = new ZKConnectionLock();

//        Long startTime = System.currentTimeMillis();
        zkConnectionLock.lock("ababab");
//        Long endTime = System.currentTimeMillis();
//        System.out.println(endTime - startTime);
    }
}
