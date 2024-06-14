package com.app.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    private static long time = 1000 * 3600;
    private static String sign = "qiyaoyyds";


    public static String createToken(){
        // 创建一个JwtBuilder对象
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .claim("phone", "136136136")
                .claim("exp", new Date(System.currentTimeMillis() + time))
                .claim("nbf", System.currentTimeMillis())
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .signWith(SignatureAlgorithm.HS256, sign)
                .compact();

        return jwtToken;
    }

    // 验证token是否过期的方法
    public static boolean checkToken(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(sign)).build();
        DecodedJWT verify = jwtVerifier.verify(token);
        long exp = verify.getClaim("exp").asLong();
        //System.out.println("exp:"+exp);
        long now = System.currentTimeMillis();
        //System.out.println("now:"+now/1000);
        if(now/1000 > exp){
            return false;
        }
        return true;
    }

    // 验证token是否完整
    public static boolean verify(String token){
        try {
            //设置签名的加密算法：HMAC256
            Algorithm algorithm = Algorithm.HMAC256(sign);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
