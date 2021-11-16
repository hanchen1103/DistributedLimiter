package Service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import javax.annotation.Resource;


public class LimitTest {

    @Resource
    JedisConnectionFactory jedisConnectionFactory;

    DistributedLimit distributedLimit = new DistributedLimit.JedisConnectionFactoryBuilder(jedisConnectionFactory).
            wasteTicket(5).maxSize(100).secondToken(5).request("test-request").create();

    public class TestLimit implements Runnable {

        @Override
        public void run() {
            Boolean res = distributedLimit.isLimit();
            System.out.println(res);
        }
    }

    @Test
    public void limitTest() {
        for(int i = 0; i < 100; i ++) {
            Thread a = new Thread(new TestLimit());
            a.start();
        }
    }
}
