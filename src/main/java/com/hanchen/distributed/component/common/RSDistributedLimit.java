package com.hanchen.distributed.component.common;


import com.hanchen.distributed.component.constant.RedisType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.commands.JedisCommands;
import util.ScriptUtil;

import java.util.*;

public class RSDistributedLimit {


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
    }

    private Object limitRequest() {
        Object res;
        List<String> keys = new ArrayList<>();
        keys.add(request);
        List<String> argv = new ArrayList<>();
        argv.add(String.valueOf(maxSize));
        argv.add(String.valueOf(secondToken));
        argv.add(String.valueOf(wasteTicket));
        if(jedis instanceof Jedis) {
            res = this.jedis.eval(script, keys, argv);
        } else if (jedis instanceof JedisCluster){
            res = this.jedis.eval(script, keys, argv);
        } else {
            return 0L;
        }
        return res;
    }

    public boolean isLimit() {
        Object res = limitRequest();
        return Objects.equals(LIMIT_CODE, res);
    }


    public static class JedisBuilder<T extends JedisCluster> {

        private String request = "index";

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

        public RSDistributedLimit create() {
            return new RSDistributedLimit(this);
        }
    }

}
