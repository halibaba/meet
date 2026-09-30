package com.meet.feign;

import com.meet.dto.LoginUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 认证服务向 admin 查询登录用户。
 */
@FeignClient(value = "meet-admin-biz", contextId = "adminUserClient")
public interface AdminUserClient {

    @GetMapping("/admin/acl/user/login/{username}")
    LoginUserDTO loadByUsername(@PathVariable("username") String username);
}
