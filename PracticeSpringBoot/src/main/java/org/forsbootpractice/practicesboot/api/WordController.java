package org.forsbootpractice.practicesboot.api;

import lombok.extern.slf4j.Slf4j;
import org.forsbootpractice.practicesboot.Service.WordService;
import org.forsbootpractice.practicesboot.dto.Toeic;
import org.forsbootpractice.practicesboot.dto.Toss;
import org.forsbootpractice.practicesboot.dto.Word;
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
@RequestMapping("/word")
@EnableAsync
public class WordController {
    private final WordService wordService;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public WordController(final WordService wordService, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.wordService = wordService;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @Async("threadPoolTaskExecutor")
    @GetMapping("")
    public CompletableFuture<ResponseEntity> getToeic(@RequestParam("category") final Optional<Integer> category) {
        CompletableFuture<ResponseEntity> ret = new CompletableFuture<>();
        try {
            switch (category.get()) {
                case 1:
                    ret = CompletableFuture.completedFuture(new ResponseEntity<>(wordService.getAllToeic().get(), HttpStatus.OK));
                    break;
                case 2:
                    ret = CompletableFuture.completedFuture(new ResponseEntity<>(wordService.getAllToss().get(),HttpStatus.OK));
            }
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
            return CompletableFuture.completedFuture(new ResponseEntity<>(wordService.getToeicsave(toeic).join(), HttpStatus.OK));
        }catch (Exception e) {
            log.error(e.getMessage());
            return CompletableFuture.completedFuture(new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
