package com.hanchen.distributed.component.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SpringRequestLimiter {

    int errorCode() default 500;

    String errMsg() default "request has been limited";

}
