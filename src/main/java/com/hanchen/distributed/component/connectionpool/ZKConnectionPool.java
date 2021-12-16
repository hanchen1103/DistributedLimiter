package com.hanchen.distributed.component.connectionpool;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ZKConnectionPool {

    private static Float extendLoadFactor = 0.75F;

    private static Float reduceLoadFactor = 0.25F;

    private static AtomicInteger connectionNum = new AtomicInteger(0);

    private static AtomicInteger capacity = new AtomicInteger(10);

    private static Queue<ZKConnectionEntity> ZKConnectionQueue;

    private static String connectionString = "127.0.0.1:2181";

    private static AtomicInteger inUseConnectionSize = new AtomicInteger(10);


    private static void connectionPollInitial() {
        int cap = capacity.get();
        ZKConnectionQueue = new ArrayBlockingQueue<>(10);
        for(int i = 0; i < cap; ++ i) {
            ZKConnectionEntity ZKConnectionEntity = new ZKConnectionEntity().connectionString(connectionString);
            new Thread(ZKConnectionEntity).start();
            ZKConnectionQueue.offer(ZKConnectionEntity);
        }
    }

    private static void checkCapacity() {
        int inUsecurSize = inUseConnectionSize.get();
        int cap = capacity.get();
        if(inUsecurSize > cap * extendLoadFactor) {
            ZKConnectionPool.capacity.doubleValue();
            Queue<ZKConnectionEntity> substituteQueue = new ArrayBlockingQueue<>(capacity.get());
            substituteQueue.addAll(ZKConnectionQueue);
            for(int i = substituteQueue.size(); i < capacity.get(); i ++) {
                ZKConnectionEntity ZKConnectionEntity = new ZKConnectionEntity().connectionString(connectionString);
                new Thread(ZKConnectionEntity).start();
                substituteQueue.offer(ZKConnectionEntity);
            }
            ZKConnectionPool.ZKConnectionQueue = substituteQueue;
            return ;
        }
        if(inUsecurSize < cap * reduceLoadFactor && inUsecurSize >= 10) {
            ZKConnectionPool.capacity.set(capacity.get() / 2);
            Queue<ZKConnectionEntity> substituteQueue = new ArrayBlockingQueue<>(capacity.get());
            for(int i = 0; i < capacity.get(); i ++) {
                substituteQueue.offer(ZKConnectionQueue.poll());
            }
            ZKConnectionPool.ZKConnectionQueue = substituteQueue;
        }

    }

    private static ZKConnectionEntity getConnection() {
        //checkCapacity();
        return ZKConnectionQueue.poll();
    }

    private static Boolean addConnection(ZKConnectionEntity zkConnectionEntity) {
        //checkCapacity();
        return ZKConnectionQueue.offer(zkConnectionEntity);
    }


    private ZKConnectionPool() {
        connectionPollInitial();
    }

    /**
     * Singleton getPool
     */
    private static class ZKConnectionPoolInstance {
        private static final ZKConnectionPool INSTANCE = new ZKConnectionPool();
    }

    public static ZKConnectionPool getInstance() {
        return ZKConnectionPoolInstance.INSTANCE;
    }

}
