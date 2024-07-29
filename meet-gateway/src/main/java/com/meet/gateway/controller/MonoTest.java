package com.meet.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.*;

/**
 * @program: meet-boot
 * @ClassName MonoTest
 * @description:
 * @author: MT
 * @create: 2023-05-23 16:59
 **/
@RestController
public class MonoTest {

    public Mono<String> getHelloMessage() {
        String message = "Hello, world!";
        return Mono.just(message);
    }

    @GetMapping("getMono2")
    public void getMono()
    {
        // 异步操作，返回Future对象
        Future<String> futureResult = performAsyncOperation();

        // 在需要的时候获取异步操作的结果
        try {
            // 可以通过isDone方法判断异步操作是否完成
            if (futureResult.isDone()) {
                String result = futureResult.get();
                System.out.println("异步操作的结果：" + result);
            } else {
                System.out.println("异步操作尚未完成");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static Future<String> performAsyncOperation() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<String> callable = () -> {
            // 模拟耗时操作
            Thread.sleep(2000);
            return "异步操作完成";
        };

        // 提交异步任务并返回Future对象
        return executorService.submit(callable);
    }

    @GetMapping("getMono3")
    public void getMono3()
    {
        // 异步操作，返回Mono对象
        Mono<String> monoResult = performAsyncOperation2();

        // 订阅Mono对象并指定回调函数
        monoResult.subscribe(
                result -> System.out.println("异步操作的结果：" + result),
                error -> error.printStackTrace()
        );

    }

    private static Mono<String> performAsyncOperation2() {
        // 创建一个Mono对象，包装异步操作
        return Mono.fromCallable(() -> {
            // 模拟耗时操作
            Thread.sleep(2000);
            return "异步操作完成";
        }).subscribeOn(Schedulers.boundedElastic());
    }
}