package org.forsbootpractice.practicesboot.api;

import lombok.extern.slf4j.Slf4j;
import org.forsbootpractice.practicesboot.Service.ToeicService;
import org.forsbootpractice.practicesboot.dto.Toeic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.forsbootpractice.practicesboot.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("/toeic")
@EnableAsync
public class ToeicController {
    private final ToeicService toeicService;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public ToeicController(final ToeicService toeicService, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.toeicService = toeicService;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @Async("threadPoolTaskExecutor")
    @GetMapping("")
    public CompletableFuture<ResponseEntity> getToeic(@RequestParam("korean") final Optional<String> korean) {
        CompletableFuture<ResponseEntity> ret = new CompletableFuture<>();
        try {
            if (korean.isPresent()) {
                //ret = CompletableFuture.completedFuture(new ResponseEntity<>(,HttpStatus.OK));
            } else
                ret = CompletableFuture.completedFuture(new ResponseEntity<>(toeicService.getAllToeic().get(), HttpStatus.OK));
        } catch (Exception e) {
            log.error("getToeic Exception {} ", e.getMessage());
            ret = CompletableFuture.completedFuture(new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR));
        }
        return ret;
    }

    @Async("threadPoolTaskExecutor")
    @PostMapping("")
    public CompletableFuture<ResponseEntity> saveToeic(Toeic toeic) {
        try {
            return CompletableFuture.completedFuture(new ResponseEntity<>(toeicService.save(toeic).join(), HttpStatus.OK));
        }catch (Exception e) {
            log.error(e.getMessage());
            return CompletableFuture.completedFuture(new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
