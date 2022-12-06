package com.example.springsecuritybasic.repository;

import com.example.springsecuritybasic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고 있음
//
public interface UserRepository extends JpaRepository<User, Integer> {

  User findByUsername(String username); // Jpa Query Method

}
