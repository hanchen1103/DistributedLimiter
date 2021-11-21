package com.hanchen.distributed.component.intercept;

import com.hanchen.distributed.component.annotation.SpringRequestLimiter;
import com.hanchen.distributed.component.common.RSDistributedLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LimiterRequestIntercept implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LimiterRequestIntercept.class);

    private RSDistributedLimit distributedLimit;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(distributedLimit == null) {
            throw new NullPointerException("distributedLimit is null");
        }
        if(handler instanceof HandlerMethod method) {

            SpringRequestLimiter annotation = method.getMethodAnnotation(SpringRequestLimiter.class);
            if(annotation == null) {
                //不做限流
                return true;
            }
            boolean isLimit = distributedLimit.isLimit();
            if(isLimit) {
                logger.warn(request.getRequestURL() + ": request has been limited");
                response.sendError(annotation.errorCode(), annotation.errMsg());
                return false;
            }
        }
        return true;
    }
}
