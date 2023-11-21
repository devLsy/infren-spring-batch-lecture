package com.dev.lsy.infrenspringbatchstudy;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class InfrenSpringBatchStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfrenSpringBatchStudyApplication.class, args);
    }

}
