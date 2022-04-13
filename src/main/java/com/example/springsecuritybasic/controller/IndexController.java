package com.example.springsecuritybasic.controller;

import com.example.springsecuritybasic.model.User;
import com.example.springsecuritybasic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @GetMapping({"", "/"})
  public String index() {
    // 뷰리졸버 : templates (prefix), .mustache (suffix) application.yml에서 생략 가능 (기본값이므로)
    return "index"; // 기본 : src/main/resources/templates/index.mustache
  }


  @GetMapping("/user")
  public @ResponseBody
  String user() {
    return "user";
  }

  @GetMapping("/admin")
  public @ResponseBody
  String admin() {
    return "admin";
  }

  @GetMapping("/manager")
  public @ResponseBody
  String manager() {
    return "manager";
  }

  // spring security가 해당주소를 낚아챔
  @GetMapping("/loginForm")
  public String loginForm() {
    return "loginForm";
  }

  @GetMapping("/joinForm")
  public String joinForm() {
    return "joinForm";
  }

  @PostMapping("/join")
  public String join(User user) {
    System.out.println(user);
    user.setRole("ROLE_USER");
    String rawPassword = user.getPassword();
    String encPassword = bCryptPasswordEncoder.encode(rawPassword);
    user.setPassword(encPassword);
    userRepository.save(user); // 패스워드가 암호화가 안되었기 때문에 Spring Security로 로그인 불가능
    return "redirect:/loginForm";
  }

  @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 여러개 걸고 싶을때
//  @Secured("ROLE_ADMIN")
  @GetMapping("/info")
  public @ResponseBody
  String data() {
    return "개인정보";
  }
}
