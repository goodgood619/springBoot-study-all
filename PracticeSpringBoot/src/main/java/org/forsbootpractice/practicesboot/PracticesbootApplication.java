package org.forsbootpractice.practicesboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PracticesbootApplication {
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.properties,"
            + "spring.config.location="+"classpath:real-application.yml";
    public static void main(String[] args) {
        new SpringApplicationBuilder(PracticesbootApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
//        SpringApplication.run(PracticesbootApplication.class, args);
    }

}
