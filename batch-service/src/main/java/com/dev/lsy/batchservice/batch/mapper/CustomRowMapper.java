package com.dev.lsy.batchservice.batch.mapper;


import com.dev.lsy.batchservice.batch.domain.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomRowMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Customer(rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("birthdate"));
    }
}
