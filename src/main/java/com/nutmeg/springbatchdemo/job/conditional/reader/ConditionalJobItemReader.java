package com.nutmeg.springbatchdemo.job.conditional.reader;

import com.nutmeg.springbatchdemo.mapper.DemoRowMapper;
import com.nutmeg.springbatchdemo.model.Demo;
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
public class ConditionalJobItemReader extends JdbcCursorItemReader<Demo> {

    private static final String GET_DEMO_BETWEEN_DATES =
            "SELECT UUID, UPDATED_AT, PRICE, IS_VALID " +
                    "FROM DEMO " +
                    "WHERE UPDATED_AT BETWEEN " +
                    "'%s' AND '%s'";

    private Demo demo;

    @Inject
    public ConditionalJobItemReader(
            @Value("#{jobParameters['fromDate']}") final String fromDate,
            @Value("#{jobParameters['toDate']}") final String toDate,
            final DataSource dataSource,
            final DemoRowMapper demoRowMapper
            ) {
        setSql(String.format(GET_DEMO_BETWEEN_DATES, fromDate, toDate));
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
