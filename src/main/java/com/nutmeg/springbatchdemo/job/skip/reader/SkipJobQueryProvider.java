package com.nutmeg.springbatchdemo.job.skip.reader;

import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

@Named
public class SkipJobQueryProvider extends SqlPagingQueryProviderFactoryBean {

    @Inject
    public SkipJobQueryProvider(DataSource dataSource) {
        setSelectClause("SELECT UUID, NAME");
        setFromClause("FROM STOCK");
        setSortKey("UUID");
        setDataSource(dataSource);
    }
}
