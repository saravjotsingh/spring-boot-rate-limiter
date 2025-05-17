package com.devsrv.rate_limiter.filter;

import ch.qos.logback.core.net.server.Client;
import com.devsrv.rate_limiter.algorithm.RateLimitAlgorithm;
import com.devsrv.rate_limiter.config.RateLimitConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;

@Component
public class RateLimit extends OncePerRequestFilter {

    @Autowired
    private RateLimitAlgorithm rateLimiter;

    @Autowired
    private RateLimitConfig config;



    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !config.isEnabled() ||
               Arrays.asList(config.getExcludePaths()).contains(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
            throws ServletException, IOException {
        
        String clientIP = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String key = String.format("rate_limit:%s:%s", clientIP, userAgent);

        if (rateLimiter.tryAcquire(key)) {
            // Add rate limit headers
            addRateLimitHeaders(response, key);
            chain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            
            long resetTime = Instant.now().getEpochSecond() + config.getWindowSeconds();
            response.setHeader("X-RateLimit-Reset", String.valueOf(resetTime));
            response.getWriter().write("{\"message\": \"API Limit Exceed\"}");
        }
    }

    private void addRateLimitHeaders(HttpServletResponse response, String key) {
        response.setHeader("X-RateLimit-Limit", String.valueOf(config.getMaxRequests()));
    }
}
