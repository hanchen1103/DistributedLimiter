package com.hanchen.distributed.component.common;


import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;
import util.ScriptUtil;

import java.util.*;

public class RSDistributedLimit {


    private JedisConnectionFactory jedisConnectionFactory;

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

    private RSDistributedLimit(JedisConnectionFactoryBuilder jedisConnectionFactoryBuilder) {
        jedisConnectionFactory = jedisConnectionFactoryBuilder.jedisConnectionFactory;
        this.request = jedisConnectionFactoryBuilder.request;
        this.maxSize = jedisConnectionFactoryBuilder.maxSize;
        this.secondToken = jedisConnectionFactoryBuilder.secondToken;
        this.wasteTicket = jedisConnectionFactoryBuilder.wasteTicket;
        loadScript();
    }

    private Object limitRequest(Object connection) {
        Object res = null;
        List<String> keys = new ArrayList<>();
        keys.add(request);
        List<String> argv = new ArrayList<>();
        argv.add(String.valueOf(maxSize));
        argv.add(String.valueOf(secondToken));
        argv.add(String.valueOf(wasteTicket));
        if(connection instanceof Jedis) {
            res = ((Jedis) connection).eval(script, keys, argv);
            ((Jedis) connection).close();
        }
        return res;
    }

    private Object getConnection() {
        Object connection;
        RedisConnection redisConnection = jedisConnectionFactory.getConnection();
        connection = redisConnection.getNativeConnection();
        return connection;
    }

    public boolean isLimit() {
        Object connection = getConnection();
        Object res = limitRequest(connection);
        return Objects.equals(LIMIT_CODE, res);
    }


    public static class JedisConnectionFactoryBuilder {

        private String request = "index";

        private int maxSize = 200;

        private int secondToken = 1;

        private int wasteTicket = 1;


        public JedisConnectionFactory jedisConnectionFactory;

        public JedisConnectionFactoryBuilder(JedisConnectionFactory jedisConnectionFactory) {
            this.jedisConnectionFactory = jedisConnectionFactory;
        }

        public JedisConnectionFactoryBuilder request(String request) {
            this.request = request;
            return this;
        }

        public JedisConnectionFactoryBuilder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public JedisConnectionFactoryBuilder secondToken(int secondToken) {
            this.secondToken = secondToken;
            return this;
        }

        public JedisConnectionFactoryBuilder wasteTicket(int wasteTicket) {
            this.wasteTicket = wasteTicket;
            return this;
        }

        public RSDistributedLimit create() {
            return new RSDistributedLimit(this);
        }
    }

}
