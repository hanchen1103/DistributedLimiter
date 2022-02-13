package ZkTest;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ZkLockWatcher extends ZKDistributedThread implements Watcher{

    private final Logger logger = LoggerFactory.getLogger(ZkLockWatcher.class);

    private static ZooKeeper zooKeeper;

    public void create(String connectionString, int sessionTimeout) {
        try {
            zooKeeper = new ZooKeeper(connectionString, sessionTimeout, new ZkLockWatcher());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public Stat exists(String path) throws InterruptedException, KeeperException {
        return zooKeeper.exists(path, true);
    }

    @Override
    public void process(WatchedEvent event) {
        logger.info("eventType: " + event.getType());
        if(event.getType() == Event.EventType.NodeDeleted) {
            try {
                zooKeeper.exists(event.getPath(), true);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }
}
