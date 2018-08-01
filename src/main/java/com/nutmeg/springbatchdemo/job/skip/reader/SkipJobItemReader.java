package com.nutmeg.springbatchdemo.job.skip.reader;

import com.nutmeg.springbatchdemo.mapper.StockRowMapper;
import com.nutmeg.springbatchdemo.model.Stock;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

@Named
public class SkipJobItemReader extends JdbcPagingItemReader<Stock>{

    @Inject
    public SkipJobItemReader(
            final DataSource dataSource,
            final PagingQueryProvider skipJobQueryProvider,
            final StockRowMapper stockRowMapper) {
        setDataSource(dataSource);
        setQueryProvider(skipJobQueryProvider);
        setRowMapper(stockRowMapper);
        setPageSize(100);
        setSaveState(false);
    }
}
