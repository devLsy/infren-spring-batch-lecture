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
                .start(step1())
                .listener(new StopWatchjobListener())
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
//                .processor(customItemProcessor())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    //비동기 스텝
    public Step asyncStep1() throws Exception{
        return stepBuilderFactory.get("asyncStep1")
                .<Customer, Customer>chunk(100)
                .reader(pagingItemReader())
								//비동기
                .processor(asyncItemProcessor())
								//비동기
                .writer(asyncItemWriter())
                .build();
    }

    private ItemWriter<? super Customer> asyncItemWriter() {
        return null;
    }

    @Bean
    //비동기 프로세서
    public AsyncItemProcessor asyncItemProcessor() {

        AsyncItemProcessor<Customer, Customer> asyncItemProcessor = new AsyncItemProcessor<>();
        //프로세서에게 프로세스 작업 위임
        asyncItemProcessor.setDelegate(customItemProcessor());
        //스레드 설정
        asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return asyncItemProcessor;
    }
    
    @Bean
    public ItemProcessor<Customer, Customer> customItemProcessor() {
        return new ItemProcessor<Customer, Customer>() {
            @Override
            public Customer process(Customer item) throws Exception {
                //잠시 멈춤
                Thread.sleep(20);
                //대문자로만 변환하는 가공
                return new Customer(item.getId(), item.getFirstName().toUpperCase(), item.getLastName().toUpperCase(), item.getBirthdate());
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
