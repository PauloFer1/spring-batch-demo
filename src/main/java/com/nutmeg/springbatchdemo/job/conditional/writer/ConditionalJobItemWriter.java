package com.nutmeg.springbatchdemo.job.conditional.writer;

import com.nutmeg.springbatchdemo.model.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Named
public class ConditionalJobItemWriter extends JdbcBatchItemWriter<Price> {

    private static final String UPDATE_IS_VALID_DEMO =
            "UPDATE PRICE SET IS_VALID = :isValid WHERE UUID = :uuid";

    @Inject
    public ConditionalJobItemWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        super.setDataSource(dataSource);
        super.setJdbcTemplate(jdbcTemplate);
        super.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        super.setSql(UPDATE_IS_VALID_DEMO);
    }

    @Override
    public void write(List<? extends Price> items) throws Exception {
        log.info("Writing {} items", items.size());
        super.write(items);
    }
}
