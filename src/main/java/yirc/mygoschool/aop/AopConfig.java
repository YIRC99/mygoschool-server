package yirc.mygoschool.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AopConfig {

    @Autowired
    private LoggingConfig loggingConfig;

    @Bean
    public LogAspect logAspect() {
        return new LogAspect(loggingConfig);
    }
}