package com.hanchen.distributed.component.watchimpl;


import com.hanchen.distributed.component.common.ZKDistributedLockImpl;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchDeleteEventImpl implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(ZKDistributedLockImpl.class);

    ZKDistributedLockImpl zkDistributedLock;

    public void setZkDistributedLock(ZKDistributedLockImpl zkDistributedLock) {
        this.zkDistributedLock = zkDistributedLock;
    }

    @Override
    public void process(WatchedEvent event) {;
        if(event.getType() == Event.EventType.NodeDeleted){
            logger.info("pre lock: " + event.getPath() + " been unlocked, current get lock is " + zkDistributedLock.getCurrentLockName());
            zkDistributedLock.setIsLock();
        }
    }


}
