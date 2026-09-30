package com.meet.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meet.pub.entity.R;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 业务服务校验用户 token。登录查询接口只给认证服务直连，不要求用户 token。
 */
@Component
@ConditionalOnProperty(prefix = "meet.auth", name = "require-user-token", havingValue = "true")
public class UserTokenFilter extends OncePerRequestFilter implements Ordered {

    private static final String LOGIN_LOOKUP_PATH = "/admin/acl/user/login/";

    private final TokenManager tokenManager;

    private final LoginSessionStore loginSessionStore;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserTokenFilter(TokenManager tokenManager, LoginSessionStore loginSessionStore) {
        this.tokenManager = tokenManager;
        this.loginSessionStore = loginSessionStore;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || isLoginLookup(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            reject(response);
            return;
        }
        String token = header.substring(7);
        try {
            String username = tokenManager.getUserInfoFromToken(token);
            List<String> permissions = loginSessionStore.getPermissions(token);
            if (username == null || username.isEmpty() || permissions == null) {
                reject(response);
                return;
            }
        } catch (RuntimeException ex) {
            reject(response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isLoginLookup(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri != null && uri.startsWith(LOGIN_LOOKUP_PATH);
    }

    private void reject(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), R.error().code(401).message("未登录"));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 20;
    }
}
