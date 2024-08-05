package com.meet.auth.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.AccessDeniedException;

/**
 * @program: meet-boot
 * @ClassName GlobalExceptionHandler
 * @description:
 * @author: MT
 * @create: 2024-08-05 21:35
 **/
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("403"); // 指向自定义的 403 错误页面
        modelAndView.addObject("error", ex.getMessage());
        return modelAndView;
    }
}