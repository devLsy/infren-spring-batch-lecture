package com.dev.lsy.infrenspringbatchstudy.batch.job.api;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.ProductVo;
import com.dev.lsy.infrenspringbatchstudy.batch.partition.ProductPartitioner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ApiStepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private int chunkSize = 10;

    @Bean
    public Step apiMasterStep() {
        return stepBuilderFactory.get("apiMasterStep")
                .partitioner(apiSlaveStep().getName(), partitioner())
                .step(apiSlaveStep())
                .gridSize(3)
                .taskExecutor(taskExcutor())
                .build();
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
}
