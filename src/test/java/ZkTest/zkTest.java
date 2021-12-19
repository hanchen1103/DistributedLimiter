package ZkTest;

import com.hanchen.distributed.component.connectionpool.ZKConnectionEntity;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class zkTest {

    /**
     * Whether to create a node (lock)
     */
    public volatile int createFlag = 1;

    /**
     * Whether to delete a node (unlock)
     */
    public volatile int deleteFlag = 1;

    static final Object obj = new Object();

    public volatile AtomicBoolean inUse = new AtomicBoolean(false);

    public boolean isuse = false;
}
