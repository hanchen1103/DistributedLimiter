package ZkTest;

import com.hanchen.distributed.component.common.ZKDistributedLock;
import org.apache.zookeeper.*;

public class testing2 {

    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        for(int i = 0; i < 20; i ++) {
            ZKDistributedLock zkDistributedLock = new ZKDistributedLock().
                    connectString("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183").
                    lockValue("chen").sessionTimeOut(4000).ZKLockBuilder();
            Boolean res = zkDistributedLock.getLock();
            System.out.println(res);
            zkDistributedLock.unLock();
        }
        Long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }

}