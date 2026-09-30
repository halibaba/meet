package com.meet.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 使用 HS512 签发和校验登录 token。密钥至少 64 字节。
 */
@Component
public class TokenManager {

    private static final int MIN_KEY_BYTES = 64;

    public static final long TOKEN_EXPIRATION_SECONDS = 24 * 60 * 60;

    private final byte[] signKey;

    private final long tokenExpiration = TOKEN_EXPIRATION_SECONDS * 1000L;

    public TokenManager(@Value("${meet.auth.token-sign-key}") String tokenSignKey) {
        byte[] keyBytes = tokenSignKey.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < MIN_KEY_BYTES) {
            throw new IllegalStateException("meet.auth.token-sign-key 至少需要 64 字节");
        }
        this.signKey = keyBytes;
    }

    public String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, signKey)
                .compact();
    }

    public String getUserInfoFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void removeToken(String token) {
    }
}
