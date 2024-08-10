package com.meet.auth.config;

import com.meet.admin.service.PermissionService;
import com.meet.admin.service.UserService;
import com.meet.auth.entity.SecurityUser;
import com.meet.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: meet-boot
 * @ClassName UserDetailsServiceImpl
 * @description:
 * @author: MT
 * @create: 2022-12-05 16:45
 **/
//@Service("userDetailsService")
public class MeetUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.selectByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("用户不存在！");
        }

        User curUser = new User();

        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(user.getId());
        SecurityUser securityUser = new SecurityUser();
        securityUser.setPermissionValueList(permissionValueList);

        return securityUser;
    }
}