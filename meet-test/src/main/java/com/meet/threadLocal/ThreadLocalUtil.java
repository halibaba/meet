package com.meet.threadLocal;

import java.util.Optional;

/**
 * @program: meet-boot
 * @ClassName ThreadLocalUtil
 * @description:
 * @author: MT
 * @create: 2023-04-03 09:30
 **/
public class ThreadLocalUtil {

    private static ThreadLocal<OxyContent> THREAD_LOCAL = new ThreadLocal<>();

    public static Integer getUserId(){
        return Optional.ofNullable(THREAD_LOCAL.get()).map(OxyContent::getUserId).orElse(null);
    }

    public static Integer getCompanyId(){
        return Optional.ofNullable(THREAD_LOCAL.get()).map(OxyContent::getCompanyId).orElse(null);
    }

    /**
     * 获取全局上下文
     */
    public static OxyContent getContext(){
        OxyContent ctx = THREAD_LOCAL.get();
        if(ctx == null){
            ctx = new OxyContent();
            THREAD_LOCAL.set(ctx);
        }
        return ctx;
    }

    /**
     * 设置全局上下文
     */
    public static void setContext(OxyContent value){
        THREAD_LOCAL.set(value);
    }

    public static void remove(){
        System.out.println(THREAD_LOCAL.get());
        THREAD_LOCAL.remove();
        System.out.println(THREAD_LOCAL.get());
    }


}