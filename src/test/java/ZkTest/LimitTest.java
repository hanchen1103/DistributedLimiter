package ZkTest;

import com.hanchen.distributed.component.common.RSDistributedLimit;
import redis.clients.jedis.Jedis;

public class LimitTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis://127.0.0.1:6379/0");
        RSDistributedLimit rsDistributedLimit =
                new RSDistributedLimit.JedisBuilder<>(jedis).
                maxSize(200).secondToken(30).wasteTicket(1).request("queuelist").create();

    }
}
