package com.gogirl.gogirl_order.order_commons.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by yinyong on 2018/9/25.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Value("${schedule.corePoolSize}")
    private int corePoolSize = 10;
    @Value("${schedule.maxPoolSize}")
    private int maxPoolSize = 200;
    @Value("${schedule.queueCapacity}")
    private int queueCapacity = 10;

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.initialize();
        return executor;
    }
}