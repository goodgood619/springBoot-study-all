package org.forsbootpractice.practicesboot.api;

import lombok.extern.slf4j.Slf4j;
import org.forsbootpractice.practicesboot.Service.JwtService;
import org.forsbootpractice.practicesboot.Service.UserService;
import org.forsbootpractice.practicesboot.model.DefaultRes;
import org.forsbootpractice.practicesboot.model.SignUpReq;
import org.forsbootpractice.practicesboot.utils.ResponseMessage;
import org.forsbootpractice.practicesboot.utils.StatusCode;
import org.forsbootpractice.practicesboot.utils.auth.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.forsbootpractice.practicesboot.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("/users")
@EnableAsync
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private ThreadPoolTaskExecutor two;
    private ThreadPoolTaskExecutor three;
    public UserController(final UserService userService, final JwtService jwtService, final ThreadPoolTaskExecutor threadPoolTaskExecutor, ThreadPoolTaskExecutor two, ThreadPoolTaskExecutor three) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.two = two;
        this.three = three;
    }

    @Async("threadPoolTaskExecutor")
    @GetMapping("")
    public CompletableFuture<ResponseEntity> getUser(@RequestParam("name") final Optional<String> name) {
        CompletableFuture<ResponseEntity> result;
        try {
            //name이 null일 경우 false, null이 아닐 경우 true
            if (name.isPresent()) {
                result = CompletableFuture.completedFuture(new ResponseEntity<>(userService.findByName(name.get()), HttpStatus.OK));
            } else {
                CompletableFuture<DefaultRes> ret = CompletableFuture.supplyAsync(() -> {
                    try {
                        log.info("one");
                        return userService.getAllUsers();
                    } catch (ExecutionException e) {
                        log.info("{}", e.getMessage());
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        log.info("{}",e.getMessage());
                        e.printStackTrace();
                    }
                    return null;
                },three).thenCompose(s -> CompletableFuture.supplyAsync(() -> {
                    log.info("two");
                    if (s.join().isEmpty()) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
                    return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, s.join());
                },two));
                result = CompletableFuture.completedFuture(new ResponseEntity<>(ret.get(), HttpStatus.OK));
            }


        } catch (Exception e) {
            log.error(e.getMessage());
            result = CompletableFuture.completedFuture(new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR));
        }
        return result;
    }

    @PostMapping("test1")
    public String test1(final SignUpReq signUpReq) {
        //application/x-www-form-urlecoded 형식
        return signUpReq.toString();
    }

    @PostMapping("test2")
    public String test2(@RequestBody final SignUpReq signUpReq) {
        //application/json 객체 형식
        return signUpReq.toString();
    }

    /**
     * 회원 가입
     *
     * @param signUpReq 회원 가입 폼
     * @param profile   프로필 사진 객체
     * @return ResponseEntity
     */
    //@RequestPart
    //value = "profile" 파일의 키 값은 profile
    //required = false 파일을 필수로 받지 않겠다.
    @PostMapping("")
    public ResponseEntity signup(
            SignUpReq signUpReq,
            @RequestPart(value = "profile", required = false) final MultipartFile profile) {
        try {
            //파일을 signUpReq에 저장
            if (profile != null) signUpReq.setProfile(profile);
            return new ResponseEntity<>(userService.save(signUpReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원 정보 수정 API
     * 인증 필요
     *
     * @param userIdx   회원 고유 번호
     * @param signUpReq 수정할 회원 정보 객체
     * @return ResponseEntity
     */

    @Auth
    @PutMapping("/{userIdx}")
    public ResponseEntity signUp(
            @PathVariable(value = "userIdx") final int userIdx,
            SignUpReq signUpReq,
            @RequestPart(value = "profile", required = false) final MultipartFile profile) {
        try {
            if (profile != null) signUpReq.setProfile(profile);
            return new ResponseEntity<>(userService.update(userIdx, signUpReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원 정보 삭제 API
     * 인증 필요
     *
     * @param userIdx 회원 고유 번호
     * @return ResponseEntity
     */

    @Auth
    @DeleteMapping("/{userIdx}")
    public ResponseEntity deleteUser(@PathVariable(value = "userIdx") final int userIdx) {
        try {
            return new ResponseEntity<>(userService.deleteByUserIdx(userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
