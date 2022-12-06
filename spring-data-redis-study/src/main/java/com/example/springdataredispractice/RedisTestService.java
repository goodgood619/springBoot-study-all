package com.example.springdataredispractice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisTestService {

    private final RedisRepository redisRepository;

    public void addItem(String id) {

        RedisItem redisItem = new RedisItem(id, "test", 30);
        redisRepository.save(redisItem);
    }

    public RedisItem getItem(String id) {
        RedisItem redisItem = redisRepository.findById(id).orElse(null);
        return redisItem;
    }
}
