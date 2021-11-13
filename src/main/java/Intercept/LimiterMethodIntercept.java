package Intercept;

import Service.DistributedLimit;
import annotation.SpringMethodLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LimiterMethodIntercept implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LimiterMethodIntercept.class);

    private DistributedLimit distributedLimit;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(distributedLimit == null) {
            throw new NullPointerException("distributedLimit is null");
        }
        if(handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;

            SpringMethodLimiter annotation = method.getMethodAnnotation(SpringMethodLimiter.class);
            if(annotation == null) {
                //不做限流
                return true;
            }


        }
        return true;
    }
}
