package org.forsbootpractice.practicesboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LoadTest {
    static AtomicInteger counter = new AtomicInteger(0);
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(100);
        RestTemplate rt =new RestTemplate();
//        String url = "http://ec2-52-79-169-96.ap-northeast-2.compute.amazonaws.com:8080/users";
        String localurl = "http://localhost:8080/users?name=으으";
//        String localurl2 = "http://localhost:8080/users";
        String localurl3 = "http://localhost:8080/users";
        CyclicBarrier barrier = new CyclicBarrier(101);

        StopWatch main = new StopWatch();
        main.start();

        for (int i = 0; i < 100; i++) {
            es.submit(() -> {
                int idx = counter.addAndGet(1);
                // blocking시키고, thread 갯수가 101개에 도달하는 순간 blocking 해제(main도 포함해서 101개임 ㅇㅇ)

                barrier.await();
                log.info("Thread " + idx);

                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
//                Map<String,String> map = new HashMap<>();
//                map.put("name","asyncpost테스트5개");
//                map.put("part","노답파트");
                testp p = new testp("asyncpost테스트5개","노답파트");
//                String res = rt.getForObject(localurl, String.class,idx);
                testp res = rt.postForObject(localurl3,p,testp.class,idx);
                stopWatch.stop();
                //testcommit
                log.info("Elapsed: {} {} / {} {}" ,idx,stopWatch.getTotalTimeSeconds(),res.name,res.part);
                return null;
            });
        }
        barrier.await();

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        main.stop();
        log.info("Total : {}",main.getTotalTimeSeconds());
    }
    private static class testp {
        String name,part;
        private testp(String name,String part) {
            this.name = name;
            this.part= part;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPart() {
            return part;
        }

        public void setPart(String part) {
            this.part = part;
        }
    }
}
