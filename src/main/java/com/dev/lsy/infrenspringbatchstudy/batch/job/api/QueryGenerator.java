package com.dev.lsy.infrenspringbatchstudy.batch.job.api;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.ProductVo;
import com.dev.lsy.infrenspringbatchstudy.batch.rowmapper.ProductRowmapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryGenerator {

    public static ProductVo[] getProductList(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<ProductVo> productList = jdbcTemplate.query("select type from product group by type", new ProductRowmapper() {
            @Override
            public ProductVo mapRow(ResultSet rs, int rowNum) throws SQLException {
                return ProductVo.builder().type(rs.getString("type")).build();
            }
        });
        return productList.toArray(new ProductVo[]{});
    }

    public static Map<String, Object> getParameterForQuery(String parameter, String value) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(parameter, value);
        return parameters;
    }
}
