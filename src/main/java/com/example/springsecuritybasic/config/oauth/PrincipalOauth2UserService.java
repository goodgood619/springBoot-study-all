package com.example.springsecuritybasic.config.oauth;

import com.example.springsecuritybasic.config.auth.PrincipalDetails;
import com.example.springsecuritybasic.model.User;
import com.example.springsecuritybasic.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    public PrincipalOauth2UserService(
        BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration());
        System.out.println("getAccessToken : " + userRequest.getAccessToken());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes : " + oAuth2User.getAttributes());

        // 회원가입 강제로 진행 예정
        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub");
        String userName = provider + "_" + providerId; // google_10102031020
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(userName);

        if (userEntity == null) {
            System.out.println("구글 로그인 최초입니다");
            userEntity = User.builder()
                .username(userName)
                .password(password)
                .email(email)
                .role(role)
                .provider(provider)
                .providerId(providerId)
                .build();

            userRepository.save(userEntity);
        } else {
            System.out.println("구글 로그인을 이미 한적이 있습니다. 당신은 이미 자동회원가입이 되어 있습니다.");
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
