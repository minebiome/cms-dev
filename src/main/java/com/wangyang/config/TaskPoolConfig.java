package com.wangyang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;

/**
 * @author wangyang
 * @date 2021/7/24
 */
@Configuration
@EnableAsync
public class TaskPoolConfig {
    @Bean
    public RejectedExecutionHandler rejectedExecutionHandler(){
        RejectedExecutionHandler rejectedExecutionHandler = new MyIgnorePolicy();
        return rejectedExecutionHandler;
    }

    @Bean
    public ThreadFactory threadFactory(){
        ThreadFactory threadFactory = new NameTreadFactory();
        return threadFactory;
    }
    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(30);
        taskExecutor.setMaxPoolSize(30);
        taskExecutor.setQueueCapacity(1000);
//        taskExecutor.setCorePoolSize(2);
//        taskExecutor.setMaxPoolSize(2);
//        taskExecutor.setQueueCapacity(10);
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setThreadFactory(threadFactory());
        taskExecutor.setRejectedExecutionHandler(rejectedExecutionHandler());
//        taskExecutor.setThreadNamePrefix("taskExecutor--");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);

        taskExecutor.setAwaitTerminationSeconds(60);
        return taskExecutor;
    }
}