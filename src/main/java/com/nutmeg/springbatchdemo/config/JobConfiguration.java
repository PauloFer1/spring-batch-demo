package com.nutmeg.springbatchdemo.config;

import javax.inject.Named;
import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    private static final int NUMBER_THREADS = 8;

    @Bean
    @Primary
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("batch");
        asyncTaskExecutor.setConcurrencyLimit(NUMBER_THREADS);
        return asyncTaskExecutor;
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(@Named("batchDataSource") DataSource batchDataSource) {
        return new DataSourceTransactionManager(batchDataSource);
    }

    @Bean
    @Primary
    public JobRepository jobRepository(PlatformTransactionManager transactionManager,
                                       @Named("batchDataSource") DataSource batchDataSource) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(batchDataSource);
        factory.setTransactionManager(transactionManager);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    @Primary
    public JobLauncher jobLauncher(TaskExecutor taskExecutor, JobRepository jobRepository) {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        simpleJobLauncher.setTaskExecutor(taskExecutor);
        return simpleJobLauncher;
    }
}
