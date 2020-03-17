package org.forsbootpractice.practicesboot.Service;

import lombok.extern.slf4j.Slf4j;
import org.forsbootpractice.practicesboot.dto.Word;
import org.forsbootpractice.practicesboot.mapper.WordMapper;
import org.forsbootpractice.practicesboot.model.DefaultRes;
import org.forsbootpractice.practicesboot.utils.ResponseMessage;
import org.forsbootpractice.practicesboot.utils.StatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@EnableAsync
public class WordService {
    private final WordMapper wordMapper;
    private ThreadPoolTaskExecutor one, two, three;

    public WordService(WordMapper wordMapper, ThreadPoolTaskExecutor one, ThreadPoolTaskExecutor two, ThreadPoolTaskExecutor three) {
        this.wordMapper = wordMapper;
        this.one = one;
        this.two = two;
        this.three = three;
    }

    @Async("two")
    public CompletableFuture<DefaultRes> getAllToeic() {
        return getDefaultResCompletableFuture(CompletableFuture.supplyAsync(wordMapper::toeicfindAll, two));
    }

    @Async("two")
    public CompletableFuture<DefaultRes> getAllToss() {
        return getDefaultResCompletableFuture(CompletableFuture.supplyAsync(wordMapper::tossfindAll, two));
    }

    private CompletableFuture<DefaultRes> getDefaultResCompletableFuture(CompletableFuture<List<Word>> listCompletableFuture) {
        return CompletableFuture.supplyAsync(() -> listCompletableFuture, one).thenApply(s -> {
            if (s.join().isEmpty()) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, s.join());
        });
    }

    @Async("two")
    public CompletableFuture<DefaultRes> save(Word toeic) {
        try {
            wordMapper.save2(toeic);
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            return CompletableFuture.supplyAsync(() -> DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR));
        }
        return CompletableFuture.supplyAsync(() -> DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER));
    }
}
