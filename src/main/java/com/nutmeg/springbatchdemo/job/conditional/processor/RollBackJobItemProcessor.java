package com.nutmeg.springbatchdemo.job.conditional.processor;

import com.nutmeg.springbatchdemo.model.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import javax.inject.Named;

@Slf4j
@Named
public class RollBackJobItemProcessor implements ItemProcessor<Price, Price> {

    @Override
    public Price process(Price price) throws Exception {
        log.info("Rollback Price {}", price.toString());
        price.setIsValid(true);
        return price;
    }
}
