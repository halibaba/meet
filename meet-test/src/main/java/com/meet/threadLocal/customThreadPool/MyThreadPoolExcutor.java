package com.meet.threadLocal.customThreadPool;

/**
 * @program: meet-boot
 * @ClassName MyThreadPoolExcutor
 * @description: 自定义线程池
 * @author: MT
 * @create: 2023-04-03 13:56
 **/
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MyThreadPoolExcutor extends ThreadPoolExecutor {


    public MyThreadPoolExcutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }


    /**
     * 线程池执行前会调用此方法
     * @param r
     * @param t
     */
    @Override
    public void beforeExecute(Thread t, Runnable r) {

        //这个任务就是我们传入的userTask任务，其中有个字段user就保存了用户登录信息
//        if(r instanceof UserTask){
//            UserTask userTask = (UserTask) r;
//            User user = userTask.getUser();
//            System.out.println("线程池的线程名字："+t.getName());
//            System.out.println("提交任务中用户信息:"+user.toString());
//        }
    }
}