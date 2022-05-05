package com.evolutivelabs.mqttmodbus.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

	@Bean(name = "taskExecutor")
	public Executor executor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(200);
		executor.setThreadNamePrefix("async-");
		executor.setKeepAliveSeconds(1);
		executor.setAwaitTerminationMillis(300);
		executor.initialize();
		return executor;
	}
}