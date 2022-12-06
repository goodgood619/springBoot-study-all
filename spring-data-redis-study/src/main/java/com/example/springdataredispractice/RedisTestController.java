package com.example.springdataredispractice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisTestController {
    private final RedisTestService redisTestService;

    @GetMapping
    public RedisItem getRedis(@RequestParam(defaultValue = "1") String key) {
        return redisTestService.getItem(key);
    }

    @PostMapping
    public void insertRedis(@RequestParam(defaultValue = "1") String key) {
        redisTestService.addItem(key);
    }

}
