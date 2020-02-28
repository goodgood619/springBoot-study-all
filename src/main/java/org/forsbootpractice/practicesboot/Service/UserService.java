package org.forsbootpractice.practicesboot.Service;

import lombok.extern.slf4j.Slf4j;
import org.forsbootpractice.practicesboot.dto.User;
import org.forsbootpractice.practicesboot.mapper.UserMapper;
import org.forsbootpractice.practicesboot.model.DefaultRes;
import org.forsbootpractice.practicesboot.model.SignUpReq;
import org.forsbootpractice.practicesboot.utils.ResponseMessage;
import org.forsbootpractice.practicesboot.utils.StatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@EnableAsync
public class UserService {
    private final UserMapper userMapper;
    private final S3FileUploadService s3FileUploadService;
    private ThreadPoolTaskExecutor one,two,three;
    /**
     * 생성자 의존성 주입
     * @param userMapper
     * @param s3FileUploadService
     * @param one
     * @param two
     * @param three
     */
    public UserService(final UserMapper userMapper, final S3FileUploadService s3FileUploadService, ThreadPoolTaskExecutor one,ThreadPoolTaskExecutor two, ThreadPoolTaskExecutor three) {
        this.userMapper = userMapper;
        this.s3FileUploadService = s3FileUploadService;
        this.one = one;
        this.two = two;
        this.three = three;
    }

    /**
     * 모든 회원 조회
     *
     * @return DefaultRes
     */
    @Async("one")
    public CompletableFuture<DefaultRes> getAllUsers() throws ExecutionException, InterruptedException {
//        CompletableFuture<List<User>> userList = CompletableFuture.allOf(userMapper.findAll());
//        CompletableFuture<DefaultRes> res = userList.thenApplyAsync(p->{
//            if(p.isEmpty()) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
//            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, userList);
//        },threadPoolTaskExecutor);
//        return res.join();
//        final List<User> userList = userMapper.findAll();
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String temp = Thread.currentThread().getName();
                log.info("first : {}",temp);
                log.info("first: {} second : {}",temp,Thread.currentThread().getName());
                return CompletableFuture.supplyAsync(userMapper::findAll,one);
        },three).thenApply(s -> {
            String temp = Thread.currentThread().getName();
            log.info("first : {}",temp);
            if (s.join().isEmpty()) {
                log.info("{}",Thread.currentThread().getName());
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
            }
            log.info("second: {} and first : {}",Thread.currentThread().getName(),temp);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, s.join());
        });
//        return CompletableFuture.supplyAsync(userMapper::findAll,one);
//        return CompletableFuture.supplyAsync(userMapper::findAll,threadPoolTaskExecutor)
//                .thenComposeAsync(result-> CompletableFuture.supplyAsync(()->{
//                    log.info("thenComposeAsync ok");
//                    if(result.isEmpty()) return DefaultRes.res(StatusCode.NOT_FOUND,ResponseMessage.NOT_FOUND_USER);
//                    return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_USER,result);
//                },threadPoolTaskExecutor));
//        log.info("use DispatcherServletThread?");
//        DefaultRes t = res.get();
//        return t;
    }

    /**
     * 이름으로 회원 조회
     *
     * @param name 이름
     * @return DefaultRes
     */
    @Async("one")
    public CompletableFuture<DefaultRes> findByName(final String name) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("one");
            return CompletableFuture.supplyAsync(()->userMapper.findByName(name),one);
        },three).thenApply(s -> {
            if (s.join() == null) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, s.join());
        });
//        final User user = userMapper.findByName(name);
//        if (user == null)
//            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
//        log.info("{}",DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, user));
//        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, user);
    }
    /**
     * 회원 가입
     *
     * @param signUpReq 회원 데이터
     * @return DefaultRes
     */
    @Transactional
    @Async("two")
    public CompletableFuture<DefaultRes> save(SignUpReq signUpReq) {
        CompletableFuture<DefaultRes> s3fileuploadasync = CompletableFuture.supplyAsync(()->{
            if(signUpReq.getProfile()!=null) {
                try {
                    signUpReq.setProfileUrl(s3FileUploadService.upload(signUpReq.getProfile()));
                } catch (IOException e) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    log.error(e.getMessage());
                }
            }
            return signUpReq;
        },two).thenApply(s->{
            try {
                userMapper.save(s);
            }
            catch (Exception e) {
                log.error("{}",e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR,ResponseMessage.DB_ERROR);
            }
            return null;
        });
        if(s3fileuploadasync == null) {
            return CompletableFuture.supplyAsync(()-> DefaultRes.res(StatusCode.CREATED,ResponseMessage.CREATED_USER));
        }
        else return s3fileuploadasync;
//        try {
//            //파일이 있다면 파일을 S3에 저장 후 경로를 저장(얘는 따로 처리해도 됨 ㅇㅇ,아래 3개로직 전부다,근데 맨 마지막 DefaultRes는 필요하긴 하지 ㅇㅇ)
//            if (signUpReq.getProfile() != null)
//                signUpReq.setProfileUrl(s3FileUploadService.upload(signUpReq.getProfile()));
//            userMapper.save(signUpReq);
//            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER);
//        } catch (Exception e) {
//            //Rollback
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            log.error(e.getMessage());
//            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
//        }
    }
    /**
     * 회원 정보 수정
     *
     * @param userIdx   회원 고유 번호
     * @param signUpReq 수정할 회원 데이터
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes update(final int userIdx, final SignUpReq signUpReq) {
        User temp = userMapper.findByUserIdx(userIdx);
        if (temp == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
        try {
            if (signUpReq.getName() != null) temp.setName(signUpReq.getName());
            if (signUpReq.getPart() != null) temp.setPart(signUpReq.getPart());
            userMapper.update(userIdx, temp);
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.UPDATE_USER);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 회원 탈퇴
     *
     * @param userIdx 회원 고유 번호
     * @return DefaultRes
     */
    @Async("one")
    @Transactional
    public CompletableFuture<DefaultRes> deleteByUserIdx(final int userIdx) {
        return CompletableFuture.supplyAsync(()-> userMapper.findByUserIdx(userIdx),one)
                .thenApply(res-> {
                    if(res == null) return DefaultRes.res(StatusCode.NOT_FOUND,ResponseMessage.NOT_FOUND_USER);
                    if(res.getProfileUrl()!= null) {
                        log.info("{}", res.getProfileUrl().substring(60));
                        s3FileUploadService.deleteOnS3(res.getProfileUrl().substring(60));
                    }
                    try {
                        userMapper.deleteByUserIdx(userIdx);
                        return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.DELETE_USER);
                    }
                    catch (Exception e) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        log.error(e.getMessage());
                        return DefaultRes.res(StatusCode.DB_ERROR,ResponseMessage.DB_ERROR);
                    }
                });
//        final User user = userMapper.findByUserIdx(userIdx);
//        if (user == null)
//            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
//        try {
//            userMapper.deleteByUserIdx(userIdx);
//            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.DELETE_USER);
//        } catch (Exception e) {
//            //Rollback
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            log.error(e.getMessage());
//            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
//        }
    }
}
