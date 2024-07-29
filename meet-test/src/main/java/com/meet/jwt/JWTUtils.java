package com.meet.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: meet-boot
 * @ClassName JWTUtils
 * @description:
 * @author: MT
 * @create: 2022-12-15 10:43
 **/
public class JWTUtils {

    private static final String SING = "!ABC";

    /**
     * 生成token
     * @param map
     * @return
     */
    public static String getToken(Map<String, String> map){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);  //默认七天过期

        JWTCreator.Builder builder = JWT.create();

        //payload
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });

        //sign
        String token = builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(SING));

        return token;
    }

    /**
     * 验证token
     * @param token
     */
    public static void verify(String token){
        JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

    /**
     * 获取token信息
     * @param token
     * @return
     */
    public static DecodedJWT getTokenInfo(String token){
        JWTVerifier build = JWT.require(Algorithm.HMAC256(SING)).build();
        DecodedJWT verify = build.verify(token);
        return verify;
    }

}