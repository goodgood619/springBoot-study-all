package com.example.springsecuritybasic.config.auth;

import com.example.springsecuritybasic.model.User;
import com.example.springsecuritybasic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Spring Security config에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 Ioc 되어있는 loadUserByUsername method가 실행됨
@Service
public class PrincipalDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  // parameter에서 넘어오는 변수명 (username)이 일치해야 한다.
  // 만약 일치하지 않으면, Spring SecurityConfig를 가서 Mapping을 별도로 해준다
  // Security Session (내부 Authentication (내부 UserDetails))
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userRepository.findByUsername(username);

    if (user != null) {
      return new PrincipalDetails(user);
    }

    return null;
  }

}
