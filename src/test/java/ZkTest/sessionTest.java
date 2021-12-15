package ZkTest;

import com.hanchen.distributed.component.connectionpool.ZKThread;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class sessionTest {

    public static ZKThread zkThread;

    public static class testThreaduse implements Runnable {

        @Override
        public void run() {
            zkThread.createFlag = true;
            zkThread.deleteFlag = true;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        Map<ZKThread, Thread> map = new HashMap<>();
        List<ZKThread> list = new ArrayList<>();


            ZKThread zkThread = new ZKThread().connectionString("127.0.0.1:2181").
                    timeOut(4000).basePath("/zookeeper/lock").lockValue(String.valueOf(0));
            Thread thread = new Thread(zkThread);
            thread.start();



    }
}
