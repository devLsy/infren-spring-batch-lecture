package com.dev.lsy.infrenspringbatchstudy.batch.job.file;


import com.dev.lsy.infrenspringbatchstudy.batch.domain.Product;
import com.dev.lsy.infrenspringbatchstudy.batch.listener.CustomItemReadListener;
import com.dev.lsy.infrenspringbatchstudy.batch.listener.CustomItemWriteListener;
import com.dev.lsy.infrenspringbatchstudy.batch.listener.JobListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
//파일에서 데이터를 읽어서 DB에 저장하는 job
public class FileJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final int chunkSize = 100;

    @Bean
    public Job fileJob() {
        return jobBuilderFactory.get("filejob")
                .incrementer(new RunIdIncrementer())
                .start(fileStep1())
                .listener(new JobListener())
                .build();
    }

    @Bean
    //일단 가공 없이 그냥 db에 저장
    public Step fileStep1() {
        return stepBuilderFactory.get("fileStep1")
                .<Product, Product>chunk(chunkSize)
                .reader(customFlatItemReader(null))
                .listener(new CustomItemReadListener())
                .writer(customFlatItemWriter())
                .listener(new CustomItemWriteListener())
                //비동기
                .taskExecutor(taskExecutor())
                .build();
    }

//    @Bean
    public TaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setThreadNamePrefix("sub-thread");
        
        return executor;
    }


    @Bean
   @StepScope
   public FlatFileItemReader<Product> customFlatItemReader(@Value("#{jobParameters['requestDate']}") String requestDate) {
       return new FlatFileItemReaderBuilder<Product>()
               .name("flatFileItemReader")
               .resource(new ClassPathResource("product_" + requestDate + ".csv"))
               .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
               .targetType(Product.class)
               .linesToSkip(1)
               .delimited().delimiter(",")
               .names("id", "name", "price", "type")
               .build();
   }

    @Bean
    public ItemWriter<Product> customFlatItemWriter() {

        return items -> {
            items.forEach(item -> log.info("item ==> [{}]", item));
        };
    }
}
