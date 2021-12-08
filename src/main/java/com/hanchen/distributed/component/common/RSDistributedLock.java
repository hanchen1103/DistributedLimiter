package com.hanchen.distributed.component.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.commands.JedisCommands;
import util.ScriptUtil;

import java.util.ArrayList;
import java.util.List;

public class RSDistributedLock {

    private static final Logger logger = LoggerFactory.getLogger(RSDistributedLock.class);

    private JedisCommands jedisCommands;

    /**
     * lua lock script
     */
    private String lockScript;

    /**
     * lua unlock script
     */
    private String unLockScript;

    /**
     * lock object
     */
    private String requestKey;

    /**
     * prevent wrong unlock
     */
    private String requestValue;

    /**
     * defaultvexpired time
     */
    private Long lockTime = 2000L;

    /**
     * if not exist
     */
    private static final String WITH_NX = "NX";

    /**
     * expired time
     */
    private static final String WITH_PX = "PX";

    private void loadScript() {
        unLockScript =  ScriptUtil.loadScript("script/unlock.lua");
        lockScript = ScriptUtil.loadScript("script/lock.lua");
    }

    public RSDistributedLock(RsLockBuilder rsLockBuilder) {
        jedisCommands = rsLockBuilder.jedis;
        requestKey = rsLockBuilder.requestKey;
        requestValue = rsLockBuilder.requestValue;
        lockTime = rsLockBuilder.lockTime;
        loadScript();
    }

    public Boolean getLock() {
        List<String> keysList = new ArrayList<>();
        keysList.add(requestKey);
        List<String> argvList = new ArrayList<>();
        argvList.add(requestKey);
        argvList.add(requestValue);
        argvList.add(String.valueOf(lockTime));
        Object res = null;
        if (jedisCommands instanceof Jedis) {
            res = ((Jedis) this.jedisCommands).eval(lockScript, keysList, argvList);
        } else if (jedisCommands instanceof JedisCluster) {
            res = ((JedisCluster) this.jedisCommands).eval(lockScript, keysList, argvList);
        }
        Boolean isLock = (res != null && !res.equals("-1"));
        if(isLock) {
            logger.info("getLock successfully");
        } else {
            logger.error("getLock failure");
        }
        return isLock;
    }


    public void unLock() {
        List<String> keysList = new ArrayList<>();
        keysList.add(requestKey);
        List<String> argvList = new ArrayList<>();
        argvList.add(requestValue);
        Object res = null;
        if (jedisCommands instanceof Jedis) {
            res = ((Jedis) this.jedisCommands).eval(unLockScript, keysList, argvList);
        } else if (jedisCommands instanceof JedisCluster) {
            res = ((JedisCluster) this.jedisCommands).eval(lockScript, keysList, argvList);
        }
        Boolean isLock = (res != null && !res.equals("-1"));

    }

    public static class RsLockBuilder<T extends JedisCommands> {

        private String requestKey;

        private String requestValue;

        private Long lockTime;

        private T jedis;

        public RsLockBuilder(T jedisCommands) {
            this.jedis = jedisCommands;
        }

        public RsLockBuilder requestKey(String requestKey) {
            this.requestKey = requestKey;
            return this;
        }

        public RsLockBuilder requestValue(String requestValue) {
            this.requestValue = requestValue;
            return this;
        }

        public RsLockBuilder lockTime(Long lockTime) {
            this.lockTime = lockTime;
            return this;
        }

        public RSDistributedLock create() {
            return new RSDistributedLock(this);
        }


    }



}
