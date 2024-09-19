package com.tigeranalytics.pricefeed.app.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "async")
public class ThreadPoolProperties {

    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer queueCapacity;
    private Integer keepAliveSeconds;
    private Integer waitTerminationSeconds;
    private String threadNamePrefix;
    private boolean waitForTasksToCompleteOnShutdown;
}
