package com.meet.threadLocal;

/**
 * @program: meet-boot
 * @ClassName WebConfig
 * @description:
 * @author: MT
 * @create: 2023-04-03 11:10
 **/

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类，配置CustomizeHeaderInterceptor拦截器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 添加Web项目的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 对所有访问路径，都通过MyInterceptor类型的拦截器进行拦截
        registry.addInterceptor(new CustomizeHeaderInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/login", "/index.html", "/user/login", "/css/**", "/images/**", "/js/**", "/fonts/**");
        //放行登录页，登陆操作，静态资源
    }
}