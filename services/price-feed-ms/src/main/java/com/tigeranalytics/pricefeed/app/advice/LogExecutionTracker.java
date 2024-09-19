package com.tigeranalytics.pricefeed.app.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogExecutionTracker {

    @Around("@annotation(com.tigeranalytics.pricefeed.app.annotation.TrackExecutionTime)")
    public Object logExecutionDuration(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("Method {}() execution takes {} ms", joinPoint.getSignature().getName(), executionTime);
        return result;
    }
}
