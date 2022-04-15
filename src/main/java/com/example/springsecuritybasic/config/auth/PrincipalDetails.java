package com.example.springsecuritybasic.config.auth;

// Spring Security가 /login 주소 요청을 낚아채서 로그인을 진행시킴
// 로그인 진행이 완료가 되면 Spring Security가 가지고 있는 session을 만들어줌 (Security ContextHolder)
// Security Session => Authentication 객체가 들어감 => UserDetails (PrincipalDetails)

import com.example.springsecuritybasic.model.User;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class PrincipalDetails implements UserDetails {

  private User user; // has-a (composition)

  public PrincipalDetails(User user) {
    this.user = user;
  }

  // 해당 User의 권한을 return 하는 곳
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collection = new ArrayList<>();
    collection.add(new GrantedAuthority() {
      @Override
      public String getAuthority() {
        return user.getRole();
      }
    });

    return collection;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  // 계정 만료 관련
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  //
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {

    // 1년동안 회원이 로그인을 안하면 휴먼 계정으로 할때
    // 현재시간 - 로그인 시간 (User의 Login시간) 이 1년 초과하면 false return

    return true;
  }
}
