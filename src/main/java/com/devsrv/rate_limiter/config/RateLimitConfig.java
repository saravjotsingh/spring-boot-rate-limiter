package com.devsrv.rate_limiter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitConfig {
    private int maxRequests = 100;
    private int windowSeconds = 60;
    private String algorithm = "sliding-window";
    private boolean enabled = true;
    private String[] excludePaths = new String[]{"/health"};
} 