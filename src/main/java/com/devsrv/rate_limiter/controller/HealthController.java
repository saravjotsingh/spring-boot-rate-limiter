package com.devsrv.rate_limiter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/health")
public class HealthController implements HealthIndicator {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    @GetMapping()
    public Health health(){
            Health.Builder healthInstance;

            boolean redisStatus = checkRedis();
            //Add Services
            if(redisStatus){ //Add  Service check here
                healthInstance = Health.up();
            }else{
                healthInstance = Health.down();
            }
            healthInstance.withDetail("Redis", redisStatus ? "UP" : "DOWN");
            return healthInstance.build();
    }

    public Boolean checkRedis(){
        String ping = redisTemplate.getConnectionFactory().getConnection().ping();
        return ping.equals("PONG");
    }

}
