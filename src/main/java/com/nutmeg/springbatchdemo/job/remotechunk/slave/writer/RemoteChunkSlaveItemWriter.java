package com.nutmeg.springbatchdemo.job.remotechunk.slave.writer;

import com.nutmeg.springbatchdemo.model.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import javax.inject.Named;
import java.util.List;

@Slf4j
@Named
public class RemoteChunkSlaveItemWriter implements ItemWriter<Price> {

    @Override
    public void write(List<? extends Price> list) throws Exception {
        log.info("Writing {} items from slave.", list.size());
    }
}
