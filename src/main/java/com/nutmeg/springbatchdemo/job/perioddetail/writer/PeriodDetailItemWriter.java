package com.nutmeg.springbatchdemo.job.perioddetail.writer;

import com.nutmeg.springbatchdemo.model.WebFund;
import java.util.List;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Slf4j
@Named
public class PeriodDetailItemWriter extends FlatFileItemWriter<WebFund> {
    private static final Resource OUTPUT_RESOURCE = new FileSystemResource("src/main/resources/period_detail.txt");

    public PeriodDetailItemWriter() {
        super.setResource(OUTPUT_RESOURCE);
        super.setAppendAllowed(true);
        super.setShouldDeleteIfExists(true);
        super.setLineAggregator( new DelimitedLineAggregator<WebFund>(){
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<WebFund>(){{
                    setNames(new String[] {"uuid", "currentModel"});
                }});
            }
        });
    }

    @Override
    public void write(List<? extends WebFund> items) throws Exception {
        super.write(items);
    }
}
