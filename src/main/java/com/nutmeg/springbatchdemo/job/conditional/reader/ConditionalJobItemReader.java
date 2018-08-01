package com.nutmeg.springbatchdemo.job.conditional.reader;

import com.nutmeg.springbatchdemo.mapper.PriceRowMapper;
import com.nutmeg.springbatchdemo.model.Price;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

@Named
@StepScope
public class ConditionalJobItemReader extends JdbcCursorItemReader<Price> {

    private static final String GET_DEMO_BETWEEN_DATES =
            "SELECT UUID, UPDATED_AT, PRICE, IS_VALID, STOCK_UUID " +
                    "FROM PRICE " +
                    "WHERE UPDATED_AT BETWEEN " +
                    "'%s' AND '%s'";

    private Price price;

    @Inject
    public ConditionalJobItemReader(
            @Value("#{jobParameters['fromDate']}") final String fromDate,
            @Value("#{jobParameters['toDate']}") final String toDate,
            final DataSource dataSource,
            final PriceRowMapper priceRowMapper
            ) {
        setSql(String.format(GET_DEMO_BETWEEN_DATES, fromDate, toDate));
        setDataSource(dataSource);
        setRowMapper(priceRowMapper);
    }

    @Override
    public Price read() throws Exception, UnexpectedInputException, ParseException {
        synchronized (this) {
            price = super.read();
        }
        return price;
    }
}
