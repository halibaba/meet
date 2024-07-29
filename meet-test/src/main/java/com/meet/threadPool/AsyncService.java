package com.meet.threadPool;

import com.meet.feign.MtUserInfoClient;
import com.meet.pub.entity.R;
import com.meet.threadLocal.OxyContent;
import com.meet.threadLocal.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @program: meet-boot
 * @ClassName AsyncService
 * @description:
 * @author: MT
 * @create: 2023-03-14 11:31
 **/
@Service
public class AsyncService {

    @Autowired
    private MtUserInfoClient mtUserInfoClient;

    @Async
    public void get2(Integer i){

        R r = mtUserInfoClient.get1(i);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("完成任务"+i);
//        System.out.println("线程" + Thread.currentThread().getName() + " 执行异步任务：" + i);
    }

    @Async
    public void testInThreadLocalGetAsy(){
        OxyContent oxyContent = ThreadLocalUtil.getContext();
        Integer userId = oxyContent.getUserId();
        System.out.println("异步方法中获取userid:" + userId);
    }
}