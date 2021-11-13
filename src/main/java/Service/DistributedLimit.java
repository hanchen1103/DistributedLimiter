package Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisConnectionFactory;

public class DistributedLimit {

    public static final Logger logger = LoggerFactory.getLogger(DistributedLimit.class);

    private JedisConnectionFactory jedisConnectionFactory;

    private DistributedLimit(JedisConnectionFactoryBuilder jedisConnectionFactoryBuilder) {
        jedisConnectionFactory = jedisConnectionFactoryBuilder.jedisConnectionFactory;
    }


    public static class JedisConnectionFactoryBuilder {
        public JedisConnectionFactory jedisConnectionFactory;

        private JedisConnectionFactoryBuilder(JedisConnectionFactory jedisConnectionFactory) {
            this.jedisConnectionFactory = jedisConnectionFactory;
        }

        public DistributedLimit create() {
            return new DistributedLimit(this);
        }
    }

}
