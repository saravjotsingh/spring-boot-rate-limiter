package com.devsrv.rate_limiter.algorithm;

import com.devsrv.rate_limiter.config.RateLimitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
public class SlidingWindowRateLimiter implements RateLimitAlgorithm {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RateLimitConfig config;


    @Override
    public boolean tryAcquire(String key) {
        long currentTimestamp = Instant.now().getEpochSecond();
        long windowStart = currentTimestamp - config.getWindowSeconds();
        
        String windowKey = "sliding:" + key;

        // Remove old timestamps
        redisTemplate.opsForZSet().removeRangeByScore(windowKey, 0, windowStart);
        
        // Get current count in window
        Long currentCount = redisTemplate.opsForZSet().count(windowKey, windowStart, currentTimestamp);

        if (currentCount != null && currentCount >= config.getMaxRequests()) {
            return false;
        }
        
        // Add new timestamp
        redisTemplate.opsForZSet().add(windowKey, String.valueOf(currentTimestamp), currentTimestamp);
        redisTemplate.expire(windowKey, config.getWindowSeconds(), TimeUnit.SECONDS);
        
        return true;
    }

} 