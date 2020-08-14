package com.topsci;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"org.edgexfoundry","com.topsci"})
@EnableDiscoveryClient
@EnableAsync
@EnableScheduling
public class BootStart {
	
	private static final Logger logger = LoggerFactory.getLogger(BootStart.class);

	public static void main(String[] args) {
		SpringApplication.run(BootStart.class, args);
		logger.info("====启动服务开始====");
	}




}
