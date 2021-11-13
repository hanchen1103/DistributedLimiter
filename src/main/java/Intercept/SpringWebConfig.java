package Intercept;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class SpringWebConfig implements WebMvcConfigurer {

    @Autowired
    LimiterMethodIntercept limiterMethodIntercept;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(limiterMethodIntercept).addPathPatterns("/**/*");
    }
}
