package com.nutmeg.springbatchdemo.job.simple.reader;

import com.nutmeg.springbatchdemo.mapper.PriceRowMapper;
import com.nutmeg.springbatchdemo.model.Price;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

@Named
public class SimpleJobItemReader extends JdbcCursorItemReader<Price> {

    private static final String GET_VALID_DEMO =
            "SELECT UUID, UPDATED_AT, PRICE, IS_VALID, STOCK_UUID " +
                    "FROM PRICE " +
                    "WHERE IS_VALID = true";

    private Price price;

    @Inject
    public SimpleJobItemReader(final DataSource dataSource, final PriceRowMapper priceRowMapper) {
        setSql(GET_VALID_DEMO);
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
