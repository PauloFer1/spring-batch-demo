package com.nutmeg.springbatchdemo.job.skip.writer;

import com.nutmeg.springbatchdemo.model.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Named
public class SkipJobItemWriter implements ItemWriter<List<Price>> {

    private static final String INSERT_PRICE =
            "INSERT INTO " +
                    "PRICE (UUID, UPDATED_AT, PRICE, IS_VALID, STOCK_UUID) " +
                    "VALUES (:uuid, :updatedAt, :price, :isValid, :stockUuid)";

    private final JdbcBatchItemWriter<Price> jdbcBatchItemWriter;

    @Inject
    public SkipJobItemWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
        jdbcBatchItemWriter.setDataSource(dataSource);
        jdbcBatchItemWriter.setJdbcTemplate(jdbcTemplate);
        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        jdbcBatchItemWriter.setSql(INSERT_PRICE);
        jdbcBatchItemWriter.afterPropertiesSet();
    }

    @Override
    public void write(List<? extends List<Price>> items) throws Exception {
        List<Price> all = items.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        log.info("Writing {} Prices", all.size());

        jdbcBatchItemWriter.write(all);
    }
}
