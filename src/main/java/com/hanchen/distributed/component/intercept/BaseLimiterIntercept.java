package com.hanchen.distributed.component.intercept;

import com.hanchen.distributed.component.common.RSDistributedLimit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class BaseLimiterIntercept {

    private static final Logger logger = LoggerFactory.getLogger(BaseLimiterIntercept.class);

    @Autowired
    RSDistributedLimit distributedLimit;

    @Pointcut("@annotation(com.hanchen.distributed.component.annotation.BaseLimiter)")
    public void check() {

    }

    @Before("check()")
    public void before(JoinPoint joinPoint) {
        if(distributedLimit == null) {
            logger.error("distributedLimit is null");
            throw new NullPointerException("distributedLimit is null");
        }

        boolean isLimit = distributedLimit.isLimit();
        if(isLimit) {
            logger.warn("request has been requested");
            throw new RuntimeException("request has benn requested");
        }
    }
}
