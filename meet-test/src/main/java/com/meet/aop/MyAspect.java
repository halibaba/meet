package com.meet.aop;

import cn.hutool.json.JSON;
import lombok.SneakyThrows;
import net.minidev.json.JSONArray;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sun.reflect.generics.tree.MethodTypeSignature;
import sun.security.util.SecurityConstants;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;

/**
 * @program: meet-boot
 * @ClassName MyAspect
 * @description:
 * @author: MT
 * @create: 2022-12-09 09:47
 **/
@Aspect
@Component
public class MyAspect {

    @Autowired
    private HttpServletRequest request;

//    @Before("execution(* com.meet.aop.SomeService.sayHello(..))")
//    public void beforeAdvice() {
//        System.out.println("beforeAdvice...");
//    }
//
//    @After("execution(* com.meet.aop.SomeService.sayHello(..))")
//    public void afterAdvice() {
//        System.out.println("afterAdvice...");
//    }

    /**
     * 执行sayHello
     * @param joinPoint
     * @return
     * @throws Throwable
     */
//    @Around("execution(* com.meet.aop.SomeService.sayHello(..))")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        System.out.println("我是环绕通知前....");
//        //执行目标函数
//        Object obj= (Object) joinPoint.proceed();
//        System.out.println("我是环绕通知后....");
//        return obj;
//    }

    /**
     * 执行sayHello2
     * @param joinPoint
     * @return
     */
    @SneakyThrows
    @Around("@within(MyInner) || @annotation(MyInner)")
    public Object around(ProceedingJoinPoint joinPoint) {

        System.out.println("我是环绕通知前....");
        //执行目标函数
        Object obj= (Object) joinPoint.proceed();

        Class<?> aClass = joinPoint.getTarget().getClass();

        String simpleName = aClass.getSimpleName();
        System.out.println("拿到类名："+simpleName);
        MyInner annotation = aClass.getAnnotation(MyInner.class);
        System.out.println("拿到类上面的注解："+annotation);
        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Method method = aClass.getMethod("sayHello2", parameterTypes);
        MyInner annotation1 = method.getAnnotation(MyInner.class);
        System.out.println("拿到方法上面的注解："+annotation1);
        String header = request.getHeader("from");
        System.out.println("拿到的请求头:"+header);
        System.out.println("我是环绕通知后....");
        return obj;
    }
}