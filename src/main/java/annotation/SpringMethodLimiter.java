package annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SpringMethodLimiter {

    int errorCode() default 500;

    String errMsg() default "request has been limited";

}
