package Service;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterTest {

    public static final RateLimiter rateLimiter = RateLimiter.create(10);

    public static void main(String[] args) {
    }

}
