package com.nutmeg.springbatchdemo.job.conditional.writer;

import com.nutmeg.springbatchdemo.model.Demo;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

@Named
public class ConditionalJobItemWriter extends JdbcBatchItemWriter<Demo> {

    private static final String UPDATE_IS_VALID_DEMO =
            "UPDATE DEMO SET IS_VALID = :isValid WHERE UUID = :uuid";

    @Inject
    public ConditionalJobItemWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        super.setDataSource(dataSource);
        super.setJdbcTemplate(jdbcTemplate);
        super.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        super.setSql(UPDATE_IS_VALID_DEMO);
    }
}
