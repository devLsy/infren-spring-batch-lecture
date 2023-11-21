package com.dev.lsy.infrenspringbatchstudy.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;

@Slf4j
@Component
public class FileJobRunner extends JobRunner {

    @Autowired
    private Scheduler scheduler;


    @Override
    protected void doRun(ApplicationArguments args) {

        String[] sourceArgs = args.getSourceArgs();

        JobDetail jobDetail = buildJobDetail(FileSchjob.class, "fileJob", "batch", new HashMap());
        Trigger trigger = buildJobTrigger("/40 * * * * ?");
        jobDetail.getJobDataMap().put("requestDate", sourceArgs[0]);
    
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.info(e.toString());
        }
    }
}
