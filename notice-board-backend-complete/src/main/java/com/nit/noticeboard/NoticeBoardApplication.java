
package com.nit.noticeboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NoticeBoardApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoticeBoardApplication.class, args);
    }
}
