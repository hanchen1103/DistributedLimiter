package com.hanchen.distributed.component.connectionpool;

import java.util.concurrent.atomic.AtomicInteger;

public class ZKConnectionPool {

    private static Float loadFacotr = 0.75F;

    private static AtomicInteger connectionNum = new AtomicInteger(0);

    private static volatile Integer capacity = 20;

    private static void connectionPollInitial() {

    }

    private ZKConnectionPool() {}

    /**
     * 单例模式获取，线程安全
     */
    private static class ZKConnectionPoolInstance {
        private static final ZKConnectionPool INSTANCE = new ZKConnectionPool();
    }

    public static ZKConnectionPool getInstance() {
        return ZKConnectionPoolInstance.INSTANCE;
    }

}
