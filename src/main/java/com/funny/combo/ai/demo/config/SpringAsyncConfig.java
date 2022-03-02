package com.funny.combo.ai.demo.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author li yao
 * @date 2022/1/13 16:12
 * @description
 */
@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        //使用Spring内置线程池任务对象
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //设置线程池参数
        int size = Runtime.getRuntime().availableProcessors();
        taskExecutor.setCorePoolSize(size);
        taskExecutor.setMaxPoolSize(2* size);
        taskExecutor.setQueueCapacity(20*size);
        taskExecutor.setRejectedExecutionHandler((r, executor) -> {
            try {
                if(!executor.isTerminated()){
                    executor.getQueue().put(r);
                }else{
                    throw new RejectedExecutionException("The ThreadPool is terminated");
                }
            } catch (InterruptedException e) {
                throw new RejectedExecutionException(e.getLocalizedMessage(), e);
            }
        });
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }

}
