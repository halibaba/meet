package com.meet.auth.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 登录会话按 token 保存。退出时删掉这一条，其他登录不受影响。
 */
@Component
public class LoginSessionStore {

    private static final String PREFIX = "login:token:";

    private final RedisTemplate redisTemplate;

    public LoginSessionStore(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String token, List<String> permissions) {
        redisTemplate.opsForValue().set(key(token), permissions, TokenManager.TOKEN_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    @SuppressWarnings("unchecked")
    public List<String> getPermissions(String token) {
        Object value = redisTemplate.opsForValue().get(key(token));
        if (value instanceof List) {
            return (List<String>) value;
        }
        return null;
    }

    public void delete(String token) {
        redisTemplate.delete(key(token));
    }

    private String key(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(PREFIX);
            for (byte item : digest) {
                builder.append(String.format("%02x", item));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 不可用", ex);
        }
    }
}
