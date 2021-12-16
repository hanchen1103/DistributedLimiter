package ZkTest;

import com.hanchen.distributed.component.connectionpool.ZKConnectionEntity;

public class zkTest {


    public static void main(String[] args) throws InterruptedException {

        ZKConnectionEntity zkThread = new ZKConnectionEntity().connectionString("127.0.0.1:2181").
                timeOut(4000).basePath("/zookeeper/lock").lockValue(String.valueOf(1));
        Thread thread = new Thread(zkThread);
        thread.start();
        zkThread.createFlag = true;
    }
}
