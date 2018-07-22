package com.nutmeg.springbatchdemo.job.simple.reader;

import com.nutmeg.springbatchdemo.mapper.DemoRowMapper;
import com.nutmeg.springbatchdemo.model.Demo;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

@Named
public class SimpleJobItemReader extends JdbcCursorItemReader<Demo> {

    private static final String GET_VALID_DEMO =
            "SELECT UUID, UPDATED_AT, PRICE, IS_VALID " +
                    "FROM DEMO " +
                    "WHERE IS_VALID = true";

    private Demo demo;

    @Inject
    public SimpleJobItemReader(final DataSource dataSource, final DemoRowMapper demoRowMapper) {
        setSql(GET_VALID_DEMO);
        setDataSource(dataSource);
        setRowMapper(demoRowMapper);
    }

    @Override
    public Demo read() throws Exception, UnexpectedInputException, ParseException {
        synchronized (this) {
            demo = super.read();
        }
        return demo;
    }
}
