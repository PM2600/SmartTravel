package com.app.interceptor;

import com.app.entity.User;
import com.app.mapper.UserMapper;
import com.app.service.ex.ServiceException;
import com.app.util.JsonResult;
import com.app.util.JwtUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader.length() <= 7){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = null;
            try {
                JSONObject json = new JSONObject();
                json.put("success", "false");
                json.put("msg", "认证失败，未通过拦截器，请重新登录");
                json.put("code", "401");
                response.getWriter().print(json);
                //System.out.println("认证失败，未通过拦截器，请重新登录");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(401);
                return false;
            }
            return false;
        }

        String token = authorizationHeader.substring("Bearer ".length());


        if (token != null) {
            boolean result = JwtUtil.verify(token);
            if (result && JwtUtil.checkToken(token)) {
                //System.out.println("通过拦截器");
                return true;
            }
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            JSONObject json = new JSONObject();
            json.put("success", "false");
            json.put("msg", "认证失败，未通过拦截器，请重新登录");
            json.put("code", "401");
            response.getWriter().print(json);
            //System.out.println("认证失败，未通过拦截器，请重新登录");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(401);
            return false;
        }
        return false;
    }
}
