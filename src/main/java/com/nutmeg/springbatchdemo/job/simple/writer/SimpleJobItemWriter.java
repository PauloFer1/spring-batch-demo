package com.nutmeg.springbatchdemo.job.simple.writer;

import com.nutmeg.springbatchdemo.model.Price;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

@Named
public class SimpleJobItemWriter extends JdbcBatchItemWriter<Price> {

    private static final String UPDATE_PRICE_DEMO =
            "UPDATE PRICE SET PRICE = :price, UPDATED_AT = :updatedAt WHERE UUID = :uuid";

    @Inject
    public SimpleJobItemWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        super.setDataSource(dataSource);
        super.setJdbcTemplate(jdbcTemplate);
        super.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        super.setSql(UPDATE_PRICE_DEMO);
    }
}
