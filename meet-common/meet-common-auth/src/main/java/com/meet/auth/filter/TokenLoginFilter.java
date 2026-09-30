package com.meet.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meet.auth.entity.SecurityUser;
import com.meet.auth.entity.User;
import com.meet.auth.security.LoginSessionStore;
import com.meet.auth.security.TokenManager;
import com.meet.auth.utils.ResponseUtil;
import com.meet.pub.entity.R;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @program: meet-boot
 * @ClassName TokenLoginFilter
 * @description:
 * @author: MT
 * @create: 2024-08-06 22:32
 **/
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private TokenManager tokenManager;
    private LoginSessionStore loginSessionStore;
    private AuthenticationManager authenticationManager;

    public TokenLoginFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, LoginSessionStore loginSessionStore){
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.loginSessionStore = loginSessionStore;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/user/login", "POST"));
    }

    //获取表单提交过来的用户名和密码

//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        //获取表单提交数据
//        try {
//            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
//            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), new ArrayList<>()));
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 获取表单提交数据
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new BadCredentialsException("用户名或密码为空");
        }

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));
    }

    //认证成功调用的方法
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 认证成功，得到认证成功之后用户信息
        SecurityUser user = (SecurityUser) authResult.getPrincipal();

        // 根据用户名生成 token
        String token = tokenManager.createToken(user.getCurrentUserInfo().getUsername());

        // 把用户名称和用户权限列表放到 redis
        loginSessionStore.save(token, user.getPermissionValueList());

        // 将 token 添加到响应头中
        response.addHeader("Authorization", "Bearer " + token);

        // 设置 SecurityContext 以便后续请求识别用户身份
        SecurityContextHolder.getContext().setAuthentication(authResult);

        // 重定向到 success.html
//        response.sendRedirect("/success.html");

        // 返回 token 到前端
        ResponseUtil.out(response, R.ok().data("token", token));
    }

    //认证失败调用的方法
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ResponseUtil.out(response, R.error());
    }
}