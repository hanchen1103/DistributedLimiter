package Service;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterTest {

    public static final RateLimiter rateLimiter = RateLimiter.create(10);

    public static void main(String[] args) {
        for(int i = 0; i < 10; i ++) {
            double timeWait = rateLimiter.acquire();
            System.out.println(timeWait);
        }
    }

}
