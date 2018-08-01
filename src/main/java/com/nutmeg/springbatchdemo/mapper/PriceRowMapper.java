package com.nutmeg.springbatchdemo.mapper;

import com.nutmeg.springbatchdemo.model.Price;
import org.springframework.jdbc.core.RowMapper;

import javax.inject.Named;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Named
public class PriceRowMapper implements RowMapper<Price> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Price mapRow(ResultSet resultSet, int i) throws SQLException {
        String date = resultSet.getString("UPDATED_AT");
        LocalDateTime updatedAt = date == null  ? null : LocalDateTime.parse(date.substring(0, Math.min(date.length(), 19)), FORMATTER);
        return Price.builder()
                .uuid(resultSet.getString("UUID"))
                .updatedAt(updatedAt)
                .price(resultSet.getBigDecimal("PRICE"))
                .isValid(resultSet.getBoolean("IS_VALID"))
                .stockUuid(resultSet.getString("STOCK_UUID"))
                .build();
    }
}
