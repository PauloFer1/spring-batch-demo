package com.nutmeg.springbatchdemo.job.twrr.reader;

import com.nutmeg.springbatchdemo.job.twrr.validation.AccountPostingLineMapper;
import com.nutmeg.springbatchdemo.model.AccountPostingCsv;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

@Named
public class TwrrFileItemReader extends FlatFileItemReader<AccountPostingCsv> {

    private static final String FILE_PATH = "twrr.csv";

    @Inject
    public TwrrFileItemReader(
            final AccountPostingLineMapper accountPostingLineMapper,
            final @Value("${accountPosting.skipLines:1}") int skipLines
    ) {
        super.setLineMapper(accountPostingLineMapper);
        super.setLinesToSkip(skipLines);
        super.setResource(new ClassPathResource(FILE_PATH));
    }
}