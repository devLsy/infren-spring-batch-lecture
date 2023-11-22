package com.dev.lsy.infrenspringbatchstudy.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class JobScheduler {

    private final JobLauncher launcher;
    private final Job filejob;

    @Scheduled(cron = "0/15 * * * * *")
    public void excuteSch() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        launcher.run(filejob, new JobParametersBuilder()
                .addString("date", LocalDateTime.now().toLocalDate().toString())
                .addString("requestDate", "20231119")
                .toJobParameters()
        );
    }
}
