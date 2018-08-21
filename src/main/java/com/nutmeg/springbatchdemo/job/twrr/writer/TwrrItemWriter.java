package com.nutmeg.springbatchdemo.job.twrr.writer;

import com.nutmeg.springbatchdemo.model.AccountPostingCsv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import javax.inject.Named;
import java.util.List;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Slf4j
@Named
public class TwrrItemWriter extends FlatFileItemWriter<AccountPostingCsv> {

    private static final Resource OUTPUT_RESOURCE = new FileSystemResource("output/outliers.csv");

    public TwrrItemWriter() {
        super.setResource(OUTPUT_RESOURCE);
        super.setAppendAllowed(true);
        super.setLineAggregator( new DelimitedLineAggregator<AccountPostingCsv>(){
            {
                setDelimiter(",");
            }
        });
    }

    @Override
    public void write(List<? extends AccountPostingCsv> list) throws Exception {
        log.info("Writing {} items", list.size());
        super.write(list);
    }
}
