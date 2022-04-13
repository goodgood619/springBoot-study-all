package com.example.springsecuritybasic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // Spring Security filter가 Spring Filter Chain에 등록되게 된다
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
// default : false, @Secured 활성화, ex) @Secured(ROLE 명시), default : false, @preAuthorize, @postAuthorize 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  // 해당 메소드의 리턴되는 object를 IoC로 등록해준다
  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable(); // security 작동안함
    http.authorizeRequests()
        .antMatchers("/user/**").authenticated() // 인증이 필요
        .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('Role_MANAGER')")
        .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
        .anyRequest().permitAll()
        .and()
        .formLogin()
        .loginPage("/loginForm")
//        .usernameParameter("username2") // controller로 들어오는 username의 변수명이 username2 일때 username으로 매핑해주는 곳임
        .loginProcessingUrl("/login") // login 주소가 호출이 되면 Spring Security가 낚아채서 대신 로그인을 진행한다
        .defaultSuccessUrl("/");
  }

}
