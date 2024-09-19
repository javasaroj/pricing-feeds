package com.tigeranalytics.pricefeed.app.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
        logger.error("Exception occurred in method - {} with params - {}", method.getName(), Arrays.toString(params));
        logger.error("Exception message - {}", throwable.getMessage(), throwable);
    }
}
