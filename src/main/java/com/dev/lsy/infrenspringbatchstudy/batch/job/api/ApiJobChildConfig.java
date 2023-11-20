package com.dev.lsy.infrenspringbatchstudy.batch.job.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ApiJobChildConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    //멀티 스레드 환경
    private final Step apiMasterStep;
    private final JobLauncher jobLauncher;
    
    //jobStep이용: step에서 job을 호출
    @Bean
    public Step jobStep() {
        return stepBuilderFactory.get("jobStep")
                .job(childJob())
                .launcher(jobLauncher)
                .build();
    }

    @Bean
    public Job childJob() {
        return jobBuilderFactory.get("childJob")
                //apiMasterStep이 서브 스레드스텝을 생성하는 주체
                .start(apiMasterStep)
                .build();
    }


}
