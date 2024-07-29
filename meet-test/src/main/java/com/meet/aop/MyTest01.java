package com.meet.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: meet-boot
 * @ClassName MyTest01
 * @description:
 * @author: MT
 * @create: 2022-12-09 09:57
 **/
@RestController
@RequestMapping("test")
public class MyTest01 {

    @Autowired
    private SomeService someService;

    @GetMapping("ok")
    public String test01(String username, HttpServletRequest request){
        request.getSession().setAttribute("username", username);
        return someService.sayHello();
    }

    @GetMapping("ok2")
    public void test02(){
        someService.sayHello2();
    }
}