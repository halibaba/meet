package com.meet.auth.security;

import com.meet.auth.utils.ResponseUtil;
import com.meet.pub.entity.R;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出时按当前 token 删除登录会话。
 */
@Component
public class TokenLogoutHandler implements LogoutHandler {

    private final TokenManager tokenManager;

    private final LoginSessionStore loginSessionStore;

    public TokenLogoutHandler(TokenManager tokenManager, LoginSessionStore loginSessionStore) {
        this.tokenManager = tokenManager;
        this.loginSessionStore = loginSessionStore;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                tokenManager.getUserInfoFromToken(token);
                loginSessionStore.delete(token);
            } catch (RuntimeException ex) {
                // 无效 token 不需要删除会话
            }
        }
        ResponseUtil.out(response, R.ok());
    }
}
