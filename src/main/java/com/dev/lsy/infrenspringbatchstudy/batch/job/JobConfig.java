package com.dev.lsy.infrenspringbatchstudy.batch.job;

import com.dev.lsy.infrenspringbatchstudy.batch.job.domain.Customer;
import com.dev.lsy.infrenspringbatchstudy.batch.job.listener.StopWatchjobListener;
import com.dev.lsy.infrenspringbatchstudy.batch.job.mapper.CustomRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
//partitioning(마스터, 슬레이브) 예제
public class JobConfig {

        private final JobBuilderFactory jobBuilderFactory;
        private final StepBuilderFactory stepBuilderFactory;
        private final DataSource dataSource;
        @Bean
        public Job job() throws Exception {
            return jobBuilderFactory.get("batchJob")
                    .incrementer(new RunIdIncrementer())
                    .start(step1())
                    .listener(new StopWatchjobListener())
                    .build();
        }

        @Bean
        public Step step1() {
            return stepBuilderFactory.get("step1")
                    .<Customer, Customer>chunk(60)
                    .reader(pagingItemReader())
                    .listener(new ItemReadListener<Customer>() {
                        @Override
                        public void beforeRead() {

                        }

                        @Override
                        public void afterRead(Customer item) {
                            log.info("Thread : " + Thread.currentThread().getName() + " item.getId()" + item.getId());
                        }

                        @Override
                        public void onReadError(Exception ex) {

                        }
                    })
                    .writer(customItemWriter())
                    .taskExecutor(taskExecutor())
                    .build();
        }

        @Bean
        public TaskExecutor taskExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(4);
            executor.setMaxPoolSize(8);
            executor.setThreadNamePrefix("non-safety-thread");
            return executor;
        }

        @Bean
        //non thread safety reader
//        public JdbcCursorItemReader<Customer> pagingItemReader() {
//
//            return new JdbcCursorItemReaderBuilder<Customer>()
//                    .fetchSize(60)
//                    .dataSource(dataSource)
//                    .rowMapper(new BeanPropertyRowMapper<>(Customer.class))
//                    .sql("select id, first_name, last_name, birthdate from customer")
//                    .name("NonSafeTyReader")
//                    .build();
//        }
        //safety thread reader
        public SynchronizedItemStreamReader<Customer> pagingItemReader() {

            JdbcCursorItemReader<Customer> nonSafeTyReader = new JdbcCursorItemReaderBuilder<Customer>()
                    .fetchSize(60)
                    .dataSource(dataSource)
                    .rowMapper(new BeanPropertyRowMapper<>(Customer.class))
                    .sql("select id, first_name, last_name, birthdate from customer")
                    .name("NonSafeTyReader")
                    .build();

            return new SynchronizedItemStreamReaderBuilder<Customer>()
                    .delegate(nonSafeTyReader)
                    .build();
        }

        @Bean
        public JdbcBatchItemWriter customItemWriter() {
            JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<>();

            itemWriter.setDataSource(dataSource);
            itemWriter.setSql("insert into customer2 values (:id, :firstName, :lastName, :birthdate)");
            itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
            itemWriter.afterPropertiesSet();

            return itemWriter;
        }
}
