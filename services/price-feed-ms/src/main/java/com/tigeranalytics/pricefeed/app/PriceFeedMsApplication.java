package com.tigeranalytics.pricefeed.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableSpringDataWebSupport
public class PriceFeedMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceFeedMsApplication.class, args);
	}

}
