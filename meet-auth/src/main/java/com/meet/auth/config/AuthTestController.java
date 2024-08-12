package com.meet.auth.config;

//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: meet-boot
 * @ClassName TestController
 * @description:
 * @author: MT
 * @create: 2022-12-05 13:42
 **/
@RestController
@RequestMapping("/oauth")
public class AuthTestController {

    @GetMapping("/token")
    public String get(){
        return "Hello token";
    }

    @GetMapping("/index")
    public String index(){
        return "Hello index";
    }

    @GetMapping("/users")
    public String users(){
        return "Hello users";
    }

    @Secured({"ROLE_test_sale"})
    @GetMapping("/update")
    public String update(){
        return "Hello Meet";
    }

//    @PreAuthorize("@pms.hasPermission('test_auth')")
    @PreAuthorize("hasAnyAuthority('test_auth')")
    @GetMapping("/update2")
    public String update2(){
        return "Hello Meet";
    }

    /**
     * user和admin角色能访问该方法
     */
    @PreAuthorize("hasAnyRole('user', 'admin')")
    @RequestMapping("/user")
    public String user(){
        System.out.println("user和admin角色访问");
        return "user和admin角色访问";
    }

    /**
     * admin角色才能访问该方法
     */
    @PreAuthorize("hasAnyRole('admin')")
    @RequestMapping("/admin")
    public String admin(){
        System.out.println("admin角色访问");
        return "admin角色访问";
    }

    /**
     * WebSecurityConfig里配置了该接口Get请求无需登录
     */
    @RequestMapping("/any")
    public String any(){
        System.out.println("这个接口Get请求无需登录");
        return "这个接口Get请求无需登录";
    }

}