package com.dev.lsy.infrenspringbatchstudy.batch.rowmapper;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.ProductVo;
import org.springframework.jdbc.core.RowMapper;

import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowmapper implements RowMapper<ProductVo> {

    @Override
    public ProductVo mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ProductVo.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .price(rs.getInt("price"))
                .type(rs.getString("type"))
                .build();
    }

}
