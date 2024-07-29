package com.meet.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.bouncycastle.math.ec.rfc8032.Ed448;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.HashMap;

/**
 * @program: meet-boot
 * @ClassName JwtTest
 * @description:
 * @author: MT
 * @create: 2022-12-13 15:56
 **/
@RestController
@RequestMapping("jwt")
public class JwtTest {

    @GetMapping("test")
    public String jwtTest(){
        HashMap<String, Object> map = new HashMap<>();

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 20);

        String token = JWT.create()
                .withHeader(map)   //header  可以不写
                .withClaim("userId", 21)      //payload
                .withClaim("username", "小黑")    //payload
                .withExpiresAt(instance.getTime())    //指定过期时间
                .sign(Algorithm.HMAC256("!ABC"));//设置签名 指定密钥

        return token;

    }

    @GetMapping("check")
    public void jwtCheck(){
        //创建验证对象
        JWTVerifier build = JWT.require(Algorithm.HMAC256("!ABC")).build();
        DecodedJWT verify = build.verify(jwtTest());
        System.out.println(verify.getClaim("userId"));
        System.out.println(verify.getClaim("username"));
    }
}