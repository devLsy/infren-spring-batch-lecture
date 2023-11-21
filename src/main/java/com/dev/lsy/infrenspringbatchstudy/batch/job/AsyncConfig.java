package com.dev.lsy.infrenspringbatchstudy.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class AsyncConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job job() throws Exception{
        return jobBuilderFactory.get("batchjob")
                .start(step1())
                .build();
    }

    @Bean
    //동기
    public Step step1() throws Exception{
        return stepBuilderFactory.get("step1")
                .chunk(100)
//                .reader(pagingItemReader())
//                .writer(customItemWriter())
                .build();
    }


    @Bean
    //비동기
    public Step asyncStep1() {
        return stepBuilderFactory.get("asyncStep1")
                .chunk(100)
//                .reader(pagingItemReader())
                .build();
    }

//    @Bean
    //jdbcPagingItemReader
//    public JdbcPagingItemReader<Customer> pagingItemReader() {
//        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
//
//        reader.setDataSource(dataSource);
//        reader.setFetchSize(300);
//        reader.setRowMapper(new CustomRowMapper());
//
//        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
//        queryProvider.setSelectClause("id, firstName, lastName, birthdate");
//        queryProvider.setFromClause("from customer");
//
//        Map<String, Order> sortKeys = new HashMap<>();
//        sortKeys.put("id", Order.ASCENDING);
//
//        return reader;
//    }

//    @Bean
    //jdbcItemWriter
//    public ItemWriter<Customer> customItemWriter() {
//        JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<>();
//
//        itemWriter.setDataSource(dataSource);
//        itemWriter.setSql("insert into customer2 values (:id, :firstName, :lastName, :birthdate)");
//        itemWriter.afterPropertiesSet();
//
//        return itemWriter;
//    }
}
