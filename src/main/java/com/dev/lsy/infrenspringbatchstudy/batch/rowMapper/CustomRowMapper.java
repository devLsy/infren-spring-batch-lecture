package com.dev.lsy.infrenspringbatchstudy.batch.rowMapper;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomRowMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Customer.builder()
                .firstName(rs.getString("firstName"))
                .lastName(rs.getString("lastName"))
                .birthdate(rs.getString("birthdate"))
                .build();
    }
}
