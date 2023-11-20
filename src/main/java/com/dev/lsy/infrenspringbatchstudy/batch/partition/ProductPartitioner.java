package com.dev.lsy.infrenspringbatchstudy.batch.partition;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.ProductVo;
import com.dev.lsy.infrenspringbatchstudy.batch.job.api.QueryGenerator;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//파티셔닝
public class ProductPartitioner implements Partitioner {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        ProductVo[] productList = QueryGenerator.getProductList(dataSource);
        Map<String, ExecutionContext> result = new HashMap<>();

        int number = 0;
        int length = productList.length;

        for (int i = 0; i < length; i++) {
            ExecutionContext value = new ExecutionContext();

            result.put("partition" + number, value);
            value.put("product", productList[i]);

            number++;
        }

        return result;
    }
}
