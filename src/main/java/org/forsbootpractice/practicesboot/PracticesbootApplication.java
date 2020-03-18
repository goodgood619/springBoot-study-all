package org.forsbootpractice.practicesboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PracticesbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticesbootApplication.class, args);
    }

}
