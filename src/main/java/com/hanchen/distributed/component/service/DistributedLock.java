package com.hanchen.distributed.component.service;

public interface DistributedLock {

    Boolean getLock();

    void unLock();

}
