package com.hanchen.distributed.component.common;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import util.ScriptUtil;

public class RSDistributedLock {

    private JedisConnectionFactory jedisConnectionFactory;

    private String script;

    private String keyRequest;

    private String valueRequest;

    private void loadScript() {
        script =  ScriptUtil.loadScript("script/lock.lua");
    }

}
