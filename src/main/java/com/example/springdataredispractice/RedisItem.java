package com.example.springdataredispractice;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "redisItem", timeToLive = 60)
public class RedisItem {

    @Id
    private String id;
    private String name;
    private Integer age;

}
