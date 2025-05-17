package com.devsrv.rate_limiter.algorithm;

public interface RateLimitAlgorithm {
    boolean tryAcquire(String key);
}