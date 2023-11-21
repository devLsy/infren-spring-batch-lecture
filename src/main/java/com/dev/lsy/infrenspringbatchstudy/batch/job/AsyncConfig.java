package com.dev.lsy.infrenspringbatchstudy.batch.job;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.Customer;
import com.dev.lsy.infrenspringbatchstudy.batch.rowMapper.CustomRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

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
                .chunk(10)
                .reader(pagingItemReader())
                .build();
    }

    @Bean
    public ItemWriter<? super Customer> customItemWriter() {

        return new JdbcBatchItemWriterBuilder<Customer>()
                .dataSource(dataSource)
                .sql("insert into customer2 values (:id, :firstName, :lastName, :birthdate)")
                .beanMapped()
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Customer> pagingItemReader() {

        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(dataSource);
        reader.setFetchSize(30);
        reader.setRowMapper(new CustomRowMapper());

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, firstName, lastName, birthdate");
        queryProvider.setFromClause("from customer");

        Map<String, Object> sortKerys = new HashMap<>();

        sortKerys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKerys);
        reader.setQueryProvider(queryProvider);

        HashMap<String, Object> parameters = new HashMap<>();

        return reader;
    }

}
