package com.dev.lsy.infrenspringbatchstudy.batch.job.api;

import com.dev.lsy.infrenspringbatchstudy.batch.chunk.processor.ApiItemProcessor1;
import com.dev.lsy.infrenspringbatchstudy.batch.chunk.processor.ApiItemProcessor2;
import com.dev.lsy.infrenspringbatchstudy.batch.chunk.processor.ApiItemProcessor3;
import com.dev.lsy.infrenspringbatchstudy.batch.classifier.ProcessorClassifier;
import com.dev.lsy.infrenspringbatchstudy.batch.domain.ApiRequestVo;
import com.dev.lsy.infrenspringbatchstudy.batch.domain.ProductVo;
import com.dev.lsy.infrenspringbatchstudy.batch.partition.ProductPartitioner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemProcessorBuilder;
import org.springframework.beans.factory.annotation.Value;
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
public class ApiStepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private int chunkSize = 10;

    @Bean
    public Step apiMasterStep() throws Exception {
        return stepBuilderFactory.get("apiMasterStep")
                .partitioner(apiSlaveStep().getName(), partitioner())
                .step(apiSlaveStep())
                .gridSize(3)
                .taskExecutor(taskExcutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExcutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //기본 스레드 3개
        taskExecutor.setCorePoolSize(3);
        //최대 6개
        taskExecutor.setMaxPoolSize(6);
        taskExecutor.setThreadNamePrefix("api-thread-");

    }


    @Bean
    public Step apiSlaveStep() throws Exception {
        return stepBuilderFactory.get("apiSlaveStep")
                .<ProductVo, ProductVo>chunk(chunkSize)
                .reader(itemReader(null))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }



    @Bean
    @StepScope
    public ItemReader<ProductVo> itemReader(@Value("#{jobParameters['product']}") String product) {

        JdbcPagingItemReader<ProductVo> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(dataSource);
        reader.setPageSize(chunkSize);
        reader.setRowMapper(new BeanPropertyRowMapper<>(ProductVo.class));

        MySqlPagingQueryProvider mySqlPagingQueryProvider = new MySqlPagingQueryProvider();
        mySqlPagingQueryProvider.setSelectClause("id, name, price, type");
        mySqlPagingQueryProvider.setFromClause("from product");
        mySqlPagingQueryProvider.setWhereClause("where type = :type");

        HashMap<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.DESCENDING);
        mySqlPagingQueryProvider.setSortKeys(sortKeys);

        reader.setParameterValues(QueryGenerator.getParameterForQuery("type", ProductVo.getType()));
        reader.setQueryProvider(mySqlPagingQueryProvider);
        reader.afterPropertiesSet();
    }

    @Bean
    public ProductPartitioner partitioner() {
        ProductPartitioner productPartitioner = new ProductPartitioner();
        productPartitioner.setDataSource(dataSource);
        return productPartitioner;
    }


    @Bean
    public ItemProcessor itemProcessor() {
        ClassifierCompositeItemProcessor<ProductVo, ApiRequestVo> processor
                = new ClassifierCompositeItemProcessor<ProductVo, ApiRequestVo>();

        ProcessorClassifier<ProductVo, ItemProcessor<?, ? extends ApiRequestVo>> classifier = new ProcessorClassifier();
        Map<String, ItemProcessor<ProductVo, ApiRequestVo>> processorMap = new HashMap<>();

        processorMap.put("1", new ApiItemProcessor1());
        processorMap.put("2", new ApiItemProcessor2());
        processorMap.put("3", new ApiItemProcessor3());

        classifier.setProcessorMap(processorMap);
        processor.setClassifier(classifier);
        
        return processor;
    }
}
