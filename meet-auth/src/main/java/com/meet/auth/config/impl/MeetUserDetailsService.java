package com.meet.auth.config.impl;

import com.meet.auth.entity.SecurityUser;
import com.meet.auth.entity.User;
import com.meet.dto.LoginUserDTO;
import com.meet.feign.AdminUserClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 登录时向 admin 查询用户和权限，组装成 SecurityUser 交给 Spring Security 比对密码。
 */
@Service("meetUserDetailsService")
public class MeetUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminUserClient adminUserClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUserDTO loginUser;
        try {
            loginUser = adminUserClient.loadByUsername(username);
        } catch (FeignException.NotFound ex) {
            throw new UsernameNotFoundException("用户不存在！");
        }
        if (loginUser == null || loginUser.getUsername() == null) {
            throw new UsernameNotFoundException("用户不存在！");
        }

        User currentUser = new User();
        currentUser.setUsername(loginUser.getUsername());
        currentUser.setPassword(loginUser.getPassword());
        currentUser.setNickName(loginUser.getNickName());
        currentUser.setSalt(loginUser.getSalt());
        currentUser.setToken(loginUser.getToken());

        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUserInfo(currentUser);
        if (loginUser.getPermissionValueList() == null) {
            securityUser.setPermissionValueList(Collections.<String>emptyList());
        } else {
            securityUser.setPermissionValueList(loginUser.getPermissionValueList());
        }
        return securityUser;
    }
}
