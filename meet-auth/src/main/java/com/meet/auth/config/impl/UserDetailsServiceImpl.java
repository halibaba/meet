package com.meet.auth.config.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

//    private MtUserInfoClient mtUserInfoClient;

    /**
     * 注入SpringSecurity内置的密码加密类
     */
    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        //用户名
//        String userName = "test";
//        //密码
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String password = passwordEncoder.encode("test");
//        //权限对象
//        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(userName);
//        //org.springframework.security.core.userdetails.User
//        return new User(userName, password, auths);
//    }

//    从数据库获取的用户名密码
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //权限对象，在SecurityConfigImpl的configure(HttpSecurity http)中配置hasAuthority()或者hasAnyAuthority()时用这个
//        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("manager");

        //在SecurityConfigImpl的configure(HttpSecurity http)中配置hasAnyRole()时用这个
//        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_sale");

        //根据用户表的类型字段做权限校验
        /**
         * id | username | password | type |
         * +----+----------+----------+------+
         * |  1 | mary     | 123456   | 1    |
         * |  2 | lucy     | 111111   | 2    |
         * |  3 | tony     | 123      | 3    |
         */
        List<GrantedAuthority> auths = new ArrayList<>();
//        auths.add(new SimpleGrantedAuthority("admin"));
        auths.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_user";
//                return "ROLE_admin";
//                return "ROLE_test_sale";
//                return "test_auth";
            }
        });
//        auths.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                switch (userInfo.getType()){
//                    case 1:return "ROLE_sale";
//                    case 2:return "manager";
//                    default:return "sale";
//                }
//
//            }
//        });
        return new User(s, new BCryptPasswordEncoder().encode("123456"), auths);
    }
}