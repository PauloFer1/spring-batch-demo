package com.nutmeg.springbatchdemo.mapper;

import com.nutmeg.springbatchdemo.model.Stock;
import org.springframework.jdbc.core.RowMapper;

import javax.inject.Named;
import java.sql.ResultSet;
import java.sql.SQLException;

@Named
public class StockRowMapper implements RowMapper<Stock> {

    @Override
    public Stock mapRow(ResultSet resultSet, int i) throws SQLException {
        return Stock.builder()
                .uuid(resultSet.getString("UUID"))
                .name(resultSet.getString("NAME"))
                .build();
    }
}
