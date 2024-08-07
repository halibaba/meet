package com.meet.auth.security;

import com.meet.auth.utils.ResponseUtil;
import com.meet.pub.entity.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: meet-boot
 * @ClassName UnAuthEntryPoint
 * @description: 未授权统一处理器
 * @author: MT
 * @create: 2024-08-06 22:27
 **/
@Component
public class UnAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseUtil.out(response, R.failed());
    }
}