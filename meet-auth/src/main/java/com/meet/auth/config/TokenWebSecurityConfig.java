package com.meet.auth.config;

import com.meet.auth.filter.TokenAuthFilter;
import com.meet.auth.filter.TokenLoginFilter;
import com.meet.auth.security.LoginSessionStore;
import com.meet.auth.security.TokenLogoutHandler;
import com.meet.auth.security.TokenManager;
import com.meet.auth.security.UnAuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @program: meet-boot
 * @ClassName TokenWebSecurityConfig
 * @description:
 * @author: MT
 * @create: 2024-08-07 21:37
 **/
@Configuration
@EnableWebSecurity
public class TokenWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private TokenManager tokenManager;
    private LoginSessionStore loginSessionStore;

    @Autowired
    @Qualifier("meetUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    public TokenWebSecurityConfig(@Qualifier("meetUserDetailsService") UserDetailsService userDetailsService,
                                  TokenManager tokenManager, LoginSessionStore loginSessionStore){
        this.userDetailsService = userDetailsService;
        this.tokenManager = tokenManager;
        this.loginSessionStore = loginSessionStore;
    }

    //设置退出的地址和token，redis操作地址
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(new UnAuthEntryPoint())
                .and().csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().logout().logoutUrl("/logout")
                .addLogoutHandler(new TokenLogoutHandler(tokenManager, loginSessionStore)).and()
                .addFilter(new TokenLoginFilter(authenticationManager(), tokenManager, loginSessionStore))
                .addFilter(new TokenAuthFilter(authenticationManager(), tokenManager, loginSessionStore));
    }

    //调用userDetailService和密码处理
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    //不进行认证的路径
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login.html");
    }
}