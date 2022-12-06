package org.forsbootpractice.practicesboot.Service;

import org.forsbootpractice.practicesboot.dto.User;
import org.forsbootpractice.practicesboot.mapper.UserMapper;
import org.forsbootpractice.practicesboot.model.DefaultRes;
import org.forsbootpractice.practicesboot.model.LoginReq;
import org.forsbootpractice.practicesboot.utils.ResponseMessage;
import org.forsbootpractice.practicesboot.utils.StatusCode;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserMapper userMapper;

    private final JwtService jwtService;

    public AuthService(final UserMapper userMapper, JwtService jwtService) {
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    /**
     * 로그인 서비스
     * @param loginReq 로그인 객체
     * @return DefaultRes
     */
    public DefaultRes<JwtService.TokenRes> login(final LoginReq loginReq) {
        final User user = userMapper.findByNameAndPassword(loginReq.getName(), loginReq.getPassword());
        if (user != null) {
            //토큰 생성
            final JwtService.TokenRes tokenDto = new JwtService.TokenRes(jwtService.create(user.getUserIdx()));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, tokenDto);
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);
    }
}
