package com.meet.auth.security;

import com.meet.auth.utils.ResponseUtil;
import com.meet.pub.entity.R;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: meet-boot
 * @ClassName TokenLogoutHandler
 * @description: 退出处理器
 * @author: MT
 * @create: 2024-08-06 22:13
 **/
@Component
public class TokenLogoutHandler implements LogoutHandler {

    private TokenManager tokenManager;

    private RedisTemplate redisTemplate;

    public TokenLogoutHandler(TokenManager tokenManager, RedisTemplate redisTemplate){
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //1.从header里面获取token

        //2.token不为空，移除token，从redis删除token

        String token = request.getHeader("token");
        if(token != null){
            //移除token
            tokenManager.removeToken(token);
            //从token获取用户名
            String username = tokenManager.getUserInfoFromToken(token);

            redisTemplate.delete(username);
        }
        ResponseUtil.out(response, R.ok());
    }
}