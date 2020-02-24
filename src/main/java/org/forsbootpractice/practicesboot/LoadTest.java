package org.forsbootpractice.practicesboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LoadTest {
    static AtomicInteger counter = new AtomicInteger(0);
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(1000);
        RestTemplate rt =new RestTemplate();
        String url = "http://localhost:8080/users";

        CyclicBarrier barrier = new CyclicBarrier(1001);

        StopWatch main = new StopWatch();
        main.start();

        for (int i = 0; i < 1000; i++) {
            es.submit(() -> {
                int idx = counter.addAndGet(1);
                // blocking시키고, thread 갯수가 101개에 도달하는 순간 blocking 해제(main도 포함해서 101개임 ㅇㅇ)

                barrier.await();
                log.info("Thread " + idx);

                StopWatch stopWatch = new StopWatch();
                stopWatch.start();


                String res = rt.getForObject(url, String.class,idx);
                stopWatch.stop();
                log.info("Elapsed: {} {} / {}" ,idx,stopWatch.getTotalTimeSeconds(),res);
                return null;
            });
        }
        barrier.await();

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        main.stop();
        log.info("Total : {}",main.getTotalTimeSeconds());
    }
}
