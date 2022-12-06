package com.example.springdataredispractice;

import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<RedisItem, String> {
}
