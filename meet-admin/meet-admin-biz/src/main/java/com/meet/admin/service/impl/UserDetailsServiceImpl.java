package com.meet.admin.service.impl;

import com.meet.admin.service.PermissionService;
import com.meet.admin.service.UserService;
import com.meet.auth.entity.SecurityUser;
import com.meet.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: meet-boot
 * @ClassName UserDetailsServiceImpl
 * @description:
 * @author: MT
 * @create: 2022-12-05 16:45
 **/
@Service("meetUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

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

        com.meet.auth.entity.User curUser = new com.meet.auth.entity.User();
        BeanUtils.copyProperties(user, curUser);

        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(user.getId());
        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUserInfo(curUser);
        securityUser.setPermissionValueList(permissionValueList);

        return securityUser;
    }
}