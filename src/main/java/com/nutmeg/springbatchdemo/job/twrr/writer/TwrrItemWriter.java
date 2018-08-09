package com.nutmeg.springbatchdemo.job.twrr.writer;

import com.nutmeg.springbatchdemo.model.AccountPostingCsv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import javax.inject.Named;
import java.util.List;

@Slf4j
@Named
public class TwrrItemWriter implements ItemWriter<AccountPostingCsv> {

    @Override
    public void write(List<? extends AccountPostingCsv> list) throws Exception {
        log.info("Writing {} items", list.size());
    }
}
