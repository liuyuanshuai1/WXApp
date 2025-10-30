package com.familypoints.backend.config;

import com.familypoints.backend.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * JWT认证过滤器
 * 用于验证JWT令牌并设置认证信息
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;

    /**
     * 构造函数
     * @param jwtUtils JWT工具类
     */
    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /**
     * 过滤器方法
     * @param request 请求
     * @param response 响应
     * @param filterChain 过滤器链
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 检查是否为公开访问路径
            String requestURI = request.getRequestURI();
            System.out.println("=== DEBUG === Processing request: " + request.getMethod() + " " + requestURI);
            System.out.println("=== DEBUG === Full request URL: " + request.getRequestURL());
            logger.info("Processing request: {} {}", request.getMethod(), requestURI);
            
            // 从请求头中获取令牌
            String token = getJwtFromRequest(request);
            
            if (token == null) {
                logger.info("No token found in request");
            } else {
                logger.info("Token found in request");
            }

            // 验证令牌
            if (token != null && jwtUtils.isTokenValid(token)) {
                // 从令牌中获取用户信息
                String userId = jwtUtils.extractUserId(token);
                String openId = jwtUtils.extractOpenId(token);

                // 创建用户详情对象
                UserDetails userDetails = new User(userId, "", new ArrayList<>());

                // 创建认证对象
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // 设置认证信息到上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 设置用户信息到请求属性中，方便后续使用
                request.setAttribute("userId", userId);
                request.setAttribute("openId", openId);
            }
        } catch (Exception ex) {
            // 记录认证失败
            logger.error("无法设置用户认证: {}", ex);
        }

        // 继续过滤链
        filterChain.doFilter(request, response);
    }



    /**
     * 从请求头中获取JWT令牌
     * @param request 请求对象
     * @return JWT令牌
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}