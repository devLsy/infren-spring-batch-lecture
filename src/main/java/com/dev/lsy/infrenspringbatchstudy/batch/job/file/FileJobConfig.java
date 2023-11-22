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
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class FileJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final int chunkSize = 100;

    @Bean
    public Job fileJob() {
        return jobBuilderFactory.get("fileJob")
                .incrementer(new RunIdIncrementer())
                .start(fileStep1())
                .listener(new JobListener())
                .build();
    }

    @Bean
    public Step fileStep1() {
        return stepBuilderFactory.get("fileStep1")
                .<Product, Product>chunk(chunkSize)
                .reader(customFlatFileItemReader(null))
                //리스너 등록
                .listener(new CustomItemReadListener())
                .writer(customFlatItemWriter())
                .listener(new CustomItemWriteListener())
                //이 옵션을 이용해서 멀티스레드 방식으로 실행
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    //여기선 스레드 설정
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //기본 스레드 4개
        taskExecutor.setCorePoolSize(4);
        //최대 8개
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setThreadNamePrefix("sub-thread");
        return taskExecutor;
    }


    @Bean
    @StepScope
    //csv파일을 읽는 리더
    public FlatFileItemReader<Product> customFlatFileItemReader(@Value("#{jobParameters['requestDate']}") String requestDate) {
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
    public JdbcBatchItemWriter<Product> customFlatItemWriter() {

        JdbcBatchItemWriter<Product> itemWriter = new JdbcBatchItemWriter<>();

        itemWriter.setDataSource(dataSource);
        itemWriter.setSql("insert into product values (:id, :name, :price, :type)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }
}
