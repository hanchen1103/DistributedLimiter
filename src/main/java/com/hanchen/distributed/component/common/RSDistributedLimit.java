package com.hanchen.distributed.component.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.commands.JedisCommands;
import util.ScriptUtil;

import java.util.*;

public class RSDistributedLimit {

    private static final Logger logger = LoggerFactory.getLogger(RSDistributedLimit.class);


    private JedisCommands jedis;


    /**
     * lua脚本
     */
    private String script;

    /**
     * 限流返回值
     */
    private static final Long LIMIT_CODE = 0L;

    /**
     * 要限流的接口
     */
    private String request;

    /**
     * 令牌桶最大容量值
     */
    private int maxSize;

    /**
     * 每秒添加的令牌
     */
    private int secondToken;

    /**
     * 每次消耗的令牌
     */
    private int wasteTicket;


    private void loadScript() {
        script =  ScriptUtil.loadScript("script/limit.lua");
    }

    private RSDistributedLimit(JedisBuilder jedisBuilder) {
        jedis = jedisBuilder.jedis;
        this.request = jedisBuilder.request;
        this.maxSize = jedisBuilder.maxSize;
        this.secondToken = jedisBuilder.secondToken;
        this.wasteTicket = jedisBuilder.wasteTicket;
        loadScript();
    }


    public void setRequest(String request) {
        this.request = request;
        logger.info("TokenBucket[request: " + request + " maxSize:" + maxSize + " addToken:" +
                secondToken + "/s wasterToken" + wasteTicket + "/s");
    }


    private Object limitRequest() {
        if(request == null || request.isEmpty()) {
            throw new NullPointerException("request should be initialized！");
        }
        Object res;
        List<String> keys = new ArrayList<>();
        keys.add(request);
        List<String> argv = new ArrayList<>();
        argv.add(String.valueOf(maxSize));
        argv.add(String.valueOf(secondToken));
        argv.add(String.valueOf(wasteTicket));
        if(jedis instanceof Jedis) {
            res = ((Jedis) this.jedis).eval(script, keys, argv);
        } else if (jedis instanceof JedisCluster){
            res = ((JedisCluster) this.jedis).eval(script, keys, argv);
        } else {
            return 0L;
        }
        return res;
    }

    public boolean isLimit() {
        Object res = limitRequest();
        return Objects.equals(LIMIT_CODE, res);
    }


    public static class JedisBuilder<T extends JedisCommands> {

        private String request;

        private int maxSize = 200;

        private int secondToken = 1;

        private int wasteTicket = 1;

        public T jedis ;

        public JedisBuilder(T jedisCommands) {
            this.jedis = jedisCommands;
        }

        public JedisBuilder request(String request) {
            this.request = request;
            return this;
        }

        public JedisBuilder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public JedisBuilder secondToken(int secondToken) {
            this.secondToken = secondToken;
            return this;
        }

        public JedisBuilder wasteTicket(int wasteTicket) {
            this.wasteTicket = wasteTicket;
            return this;
        }

        public JedisBuilder deleteRequest(Jedis jedisClient, JedisCluster jedisClusterClient) {
            if(request == null || request.isEmpty()) {
                return this;
            }
            if(jedisClient != null) {
                logger.warn("request wasalready existed, it's will be overwritten");
                jedisClient.hdel("token-bucket-remainticket", request);
                jedisClient.hdel("token-bucket-premicrosecond", request);
            } else if (jedisClusterClient != null) {
                logger.warn("request wasalready existed, it's will be overwritten");
                jedisClusterClient.hdel("token-bucket-remainticket", request);
                jedisClusterClient.hdel("token-bucket-premicrosecond", request);
            }
            return this;
        }

        public RSDistributedLimit create() {
            return new RSDistributedLimit(this);
        }

    }

}
