package com.nutmeg.springbatchdemo.config;

import javax.inject.Named;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfiguration {

    @Bean
    Jdbi jdbi(@Named("nutmegDataSource") DataSource nutmegDataSource) {
        return Jdbi.create(nutmegDataSource)
                .installPlugin(new SqlObjectPlugin());
    }

    @Bean(name = "batchDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.batch-datasource")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "nutmegDataSource")
    @ConfigurationProperties(prefix = "spring.nutmeg-datasource")
    public DataSource nutmegDataSource() {
        return DataSourceBuilder.create().build();
    }

}
