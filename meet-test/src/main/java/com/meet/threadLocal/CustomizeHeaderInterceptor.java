package com.meet.threadLocal;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import reactor.util.annotation.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @program: meet-boot
 * @ClassName CustomizeHeaderInterceptor
 * @description: 配置拦截器，需添加到WebConfig配置中，否则拦截器不生效
 * @author: MT
 * @create: 2023-04-03 10:29
 **/
@ConditionalOnBean(CustomizeHeaderFilter.class)
@Order(Integer.MIN_VALUE)
public class CustomizeHeaderInterceptor implements HandlerInterceptor {

    /**
     * 访问控制器方法前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println(new Date() + "--preHandle:" + request.getRequestURL());
        return true;
    }

    /**
     * 访问控制器方法后执行
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        System.out.println(new Date() + "--postHandle:" + request.getRequestURL());
    }

    /**
     * postHandle方法执行完成后执行，一般用于释放资源
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        ThreadLocalUtil.remove();
        System.out.println(new Date() + "--afterCompletion:" + request.getRequestURL());
    }

}