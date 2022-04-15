package com.example.springsecuritybasic.config;

import com.example.springsecuritybasic.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//1. 코드 받기(인증) 2. 액세스 토큰 (권한) ,
// 3. 사용자 프로필 정보 가져와서 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
// 4-2 (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집주소), 백화점몰 -> (vip등급, 일반등급)

@Configuration
@EnableWebSecurity // Spring Security filter가 Spring Filter Chain에 등록되게 된다
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
// default : false, @Secured 활성화, ex) @Secured(ROLE 명시), default : false, @preAuthorize, @postAuthorize 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private PrincipalOauth2UserService principalOauth2UserService;

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
            .defaultSuccessUrl("/")
            .and()
            .oauth2Login()
            .loginPage("/loginForm")
            .userInfoEndpoint()
            .userService(principalOauth2UserService); // google 로그인이 완료된 뒤의 후처리가 필요함. tip) 코드 X, accessToken + 사용자프로필정보를 받는다
  }

}
