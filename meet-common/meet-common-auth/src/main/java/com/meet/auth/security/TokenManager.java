package com.meet.auth.security;

import io.jsonwebtoken.CompressionCodec;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: meet-boot
 * @ClassName TokenManager
 * @description:
 * @author: MT
 * @create: 2024-08-06 22:02
 **/
@Component
public class TokenManager {

    //token有效时长设置
    private long tokenExpiration = 24*60*60*1000;

    //编码密钥
    private String tokenSignKey = "123456";

    //1.使用jwt根据用户名生成token
    public String createToken(String username){
        String token = Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
        return token;
    }

    //2.根据token字符串得到用户信息
    public String getUserInfoFromToken(String token){
        String userinfo = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
        return userinfo;
    }

    //删除token
    public void removeToken(String token){

    }
}