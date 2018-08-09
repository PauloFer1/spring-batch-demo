package com.nutmeg.springbatchdemo.job.twrr.reader;

import com.nutmeg.springbatchdemo.job.twrr.validation.AccountPostingLineMapper;
import com.nutmeg.springbatchdemo.model.AccountPostingCsv;
import java.nio.charset.StandardCharsets;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

@Named
public class TwrrFileItemReader extends FlatFileItemReader<AccountPostingCsv> {

    @Inject
    public TwrrFileItemReader(
        final AccountPostingLineMapper accountPostingLineMapper,
        final @Value("${accountPosting.skipLines:1}") int skipLines
    ) {
        super.setLineMapper(accountPostingLineMapper);
        super.setLinesToSkip(skipLines);
    }

    public void setResource(String resourceAsString) {
        Resource resource= new ByteArrayResource(
            resourceAsString.getBytes(StandardCharsets.UTF_8)
        );
        super.setResource(resource);
    }
}
