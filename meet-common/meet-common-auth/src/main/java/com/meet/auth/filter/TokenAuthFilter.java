package com.meet.auth.filter;

import com.meet.auth.security.TokenManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @program: meet-boot
 * @ClassName TokenAuthFilter
 * @description:
 * @author: MT
 * @create: 2024-08-07 21:22
 **/
public class TokenAuthFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenAuthFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, RedisTemplate redisTemplate){
        super(authenticationManager);
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 尝试从 Authorization 头中获取 token
        UsernamePasswordAuthenticationToken authRequest = getAuthentication(request);

        if (authRequest == null) {
            // 如果没有携带 token 或 token 不合法，返回未认证的状态
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is missing or invalid");
            return;
        }

        // 将认证信息放入安全上下文中
        SecurityContextHolder.getContext().setAuthentication(authRequest);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            String username = tokenManager.getUserInfoFromToken(token);

            // 从 redis 获取对应权限列表
            List<String> permissionValueList = (List<String>) redisTemplate.opsForValue().get(username);
            if (permissionValueList != null) {
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                for (String permissionValue : permissionValueList) {
                    authorities.add(new SimpleGrantedAuthority(permissionValue));
                }
                return new UsernamePasswordAuthenticationToken(username, token, authorities);
            }
        }
        return null;
    }

}