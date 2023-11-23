package com.dev.lsy.infrenspringbatchstudy.batch.job;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.Customer;
import com.dev.lsy.infrenspringbatchstudy.batch.listener.CustomItemReadListener;
import com.dev.lsy.infrenspringbatchstudy.batch.listener.CustomItemWriterListener;
import com.dev.lsy.infrenspringbatchstudy.batch.listener.CustomItermProcessorListener;
import com.dev.lsy.infrenspringbatchstudy.batch.listener.StopWatchjobListener;
import com.dev.lsy.infrenspringbatchstudy.batch.mapper.CustomRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AsyncConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    /**
     * job 
     * @return
     * @throws Exception
     */
    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
//                .start(step1())
                .start(asyncFileStep1())
                .listener(new StopWatchjobListener())
                .build();
    }

    private Step asyncFileStep1() {
        return stepBuilderFactory.get("asyncFileStep1")
                .<Customer, Customer>chunk(100)
                .reader(pagingItemReader())
                .listener(new CustomItemReadListener())
                .processor(asyncItemProcessor())
                .listener(new CustomItermProcessorListener())
                .writer(asyncItemWriter())
                .listener(new CustomItemWriterListener())
                .build();
    }

    /**
     * 동기 step 
     * @return
     * @throws Exception
     */
    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .<Customer, Customer>chunk(100)
                .reader(pagingItemReader())
                .listener(new CustomItemReadListener())
                .processor(customItemProcessor())
                .listener(new CustomItermProcessorListener())
                .writer(customItemWriter())
                .listener(new CustomItemWriterListener())
//                .taskExecutor(taskExecutor())
                .build();
    }

    /**
     * 비동기 step 
     * @return
     * @throws Exception
     */
    @Bean
    public Step asyncStep1() throws Exception{
        return stepBuilderFactory.get("asyncStep1")
                .<Customer, Customer>chunk(100)
                .reader(pagingItemReader())
                .processor(asyncItemProcessor())
                .writer(asyncItemWriter())
                .build();
    }

    @Bean
    public ItemWriter asyncItemWriter() {
        AsyncItemWriter<Customer> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(customItemWriter());
        return asyncItemWriter;
    }

    @Bean
    public ItemProcessor asyncItemProcessor() {
        AsyncItemProcessor<Customer, Customer> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(customItemProcessor());
        asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return asyncItemProcessor;
    }

    @Bean
    public ItemProcessor<Customer, Customer> customItemProcessor() {
        return new ItemProcessor<Customer, Customer>() {
            @Override
            public Customer process(Customer item) throws Exception {
                Thread.sleep(30);
                return new Customer(item.getId(), item.getFirstName() + "수정", item.getLastName() + "수정", item.getBirthdate());
            }
        };
    }


    /**
     * 커스텀 writer
     * @return
     */
    @Bean
    public JdbcBatchItemWriter customItemWriter() {
        JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<>();

        itemWriter.setDataSource(dataSource);
        itemWriter.setSql("insert into customer2 values (:id, :firstName, :lastName, :birthdate)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    /**
     * 동기 reader
     * @return
     */
    @Bean
    public JdbcPagingItemReader<Customer> pagingItemReader() {
        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(dataSource);
        reader.setFetchSize(300);
        reader.setRowMapper(new CustomRowMapper());

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, first_name, last_name, birthdate");
        queryProvider.setFromClause("from customer");

        Map<String, Order> sortKeys = new HashMap<>(1);

        sortKeys.put("id", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);
        reader.setQueryProvider(queryProvider);

        return reader;
    }
}
