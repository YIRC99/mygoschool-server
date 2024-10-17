package yirc.mygoschool.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import yirc.mygoschool.anno.Log;

@Aspect
@Component
public class LogAspect {

    private LoggingConfig loggingConfig;

    public LogAspect(LoggingConfig loggingConfig) {
        this.loggingConfig = loggingConfig;
    }

    @Around("@annotation(log)")
    public Object around(ProceedingJoinPoint joinPoint, Log log) throws Throwable {
        if (!loggingConfig.isEnabled()) {
            return joinPoint.proceed();
        }
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            System.out.println("Method " + joinPoint.getSignature().getName() + " took " + (end - start) + " ms");
        }
    }
}