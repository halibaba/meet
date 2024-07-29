package com.meet.threadPool;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: meet-boot
 * @ClassName AsyncConfig
 * @description:
 * @author: MT
 * @create: 2023-03-14 11:13
 **/
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    // ThredPoolTaskExcutor的处理流程
    // 当池子大小小于corePoolSize，就新建线程，并处理请求
    // 当池子大小等于corePoolSize，把请求放入workQueue中，池子里的空闲线程就去workQueue中取任务并处理
    // 当workQueue放不下任务时，就新建线程入池，并处理请求，如果池子大小撑到了maximumPoolSize，就用RejectedExecutionHandler来做拒绝处理
    // 当池子的线程数大于corePoolSize时，多余的线程会等待keepAliveTime长时间，如果无请求可处理就自行销毁

    @Override
    @Bean
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数：线程池创建的时候初始化的线程数
        executor.setCorePoolSize(2);
        // 最大线程数：线程池最大的线程数，只有缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(5);
        // 缓冲队列：用来缓冲执行任务的队列
        executor.setQueueCapacity(2);
        // 线程池关闭：等待所有任务都完成再关闭
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间：等待5秒后强制停止
        executor.setAwaitTerminationSeconds(5);
        // 允许空闲时间：超过核心线程之外的线程到达60秒后会被销毁
        executor.setKeepAliveSeconds(60);
        // 线程名称前缀
        executor.setThreadNamePrefix("learn-Async-");
        // 初始化线程
        executor.initialize();
        //拒绝策略
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                // 拒绝策略的逻辑
                System.out.println("超出营业范围");
            }
        });
        //Lamba表达式
//        executor.setRejectedExecutionHandler((r, executor1) -> {
//            // 拒绝策略的逻辑
//            System.out.println("超出营业范围");
//        });
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}