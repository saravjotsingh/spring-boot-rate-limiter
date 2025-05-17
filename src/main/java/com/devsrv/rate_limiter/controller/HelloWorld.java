package com.devsrv.rate_limiter.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloWorld {
    @GetMapping
    public ResponseEntity<?> helloWord(){
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }

}
